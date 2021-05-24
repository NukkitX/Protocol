package com.nukkitx.protocol.bedrock;

import com.nukkitx.natives.sha256.Sha256;
import com.nukkitx.natives.util.Natives;
import com.nukkitx.network.SessionConnection;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.MinecraftSession;
import com.nukkitx.protocol.bedrock.annotation.NoEncryption;
import com.nukkitx.protocol.bedrock.compat.BedrockCompat;
import com.nukkitx.protocol.handler.BatchHandler;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.handler.DefaultBatchHandler;
import com.nukkitx.protocol.bedrock.util.EncryptionUtils;
import com.nukkitx.protocol.bedrock.wrapper.BedrockWrapperSerializer;
import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.EventLoop;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;
import java.util.zip.Deflater;

public abstract class BedrockSession implements MinecraftSession<BedrockPacket> {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(BedrockSession.class);
    private static final ThreadLocal<Sha256> HASH_LOCAL;

    private final Set<Consumer<DisconnectReason>> disconnectHandlers = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Queue<BedrockPacket> queuedPackets = PlatformDependent.newMpscQueue();
    private final AtomicLong sentEncryptedPacketCount = new AtomicLong();
    private final BedrockWrapperSerializer wrapperSerializer;
    private final EventLoop eventLoop;
    final SessionConnection<ByteBuf> connection;
    private BedrockPacketCodec packetCodec = BedrockCompat.COMPAT_CODEC;
    private BedrockPacketHandler packetHandler;
    private BatchHandler<BedrockSession, BedrockPacket> batchHandler = DefaultBatchHandler.INSTANCE;
    private Cipher encryptionCipher = null;
    private Cipher decryptionCipher = null;
    private SecretKey agreedKey;
    private int compressionLevel = Deflater.DEFAULT_COMPRESSION;
    private volatile boolean closed = false;
    private volatile boolean logging = true;

    private AtomicInteger hardcodedBlockingId = new AtomicInteger(-1);

    static {
        // Required for Android API versions prior to 26.
        HASH_LOCAL = new ThreadLocal<Sha256>() {
            @Override
            protected Sha256 initialValue() {
                return Natives.SHA_256.get();
            }
        };
    }

    BedrockSession(SessionConnection<ByteBuf> connection, EventLoop eventLoop, BedrockWrapperSerializer serializer) {
        this.connection = connection;
        this.eventLoop = eventLoop;
        this.wrapperSerializer = serializer;
    }

    public void setPacketHandler(@Nonnull BedrockPacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }

    public void setPacketCodec(BedrockPacketCodec packetCodec) {
        this.packetCodec = Objects.requireNonNull(packetCodec, "packetCodec");
    }

    void checkForClosed() {
        if (this.closed) {
            throw new IllegalStateException("Connection has been closed");
        }
    }

    @Override
    public void sendPacket(@Nonnull BedrockPacket packet) {
        this.checkPacket(packet);

        this.queuedPackets.add(packet);
    }

    @Override
    public void sendPacketImmediately(@Nonnull BedrockPacket packet) {
        this.checkPacket(packet);

        this.sendWrapped(Collections.singletonList(packet), !packet.getClass().isAnnotationPresent(NoEncryption.class));
    }

    private void checkPacket(BedrockPacket packet) {
        this.checkForClosed();
        Objects.requireNonNull(packet, "packet");

        if (log.isTraceEnabled() && this.logging) {
            String to = this.connection.getAddress().toString();
            log.trace("Outbound {}: {}", to, packet);
        }

        // Verify that the packet ID exists.
        this.packetCodec.getId(packet);
    }

    public void sendWrapped(Collection<BedrockPacket> packets, boolean encrypt) {
        ByteBuf compressed = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            this.wrapperSerializer.serialize(compressed, this.packetCodec, packets, this.compressionLevel, this);
            this.sendWrapped(compressed, encrypt);
        } catch (Exception e) {
            log.error("Unable to compress packets", e);
        } finally {
            if (compressed != null) {
                compressed.release();
            }
        }
    }

    public synchronized void sendWrapped(ByteBuf compressed, boolean encrypt) {
        Objects.requireNonNull(compressed, "compressed");
        try {
            ByteBuf finalPayload = ByteBufAllocator.DEFAULT.ioBuffer(1 + compressed.readableBytes() + 8);
            finalPayload.writeByte(0xfe); // Wrapped packet ID
            if (this.encryptionCipher != null && encrypt) {
                ByteBuffer trailer = ByteBuffer.wrap(this.generateTrailer(compressed));

                ByteBuffer outBuffer = finalPayload.internalNioBuffer(1, compressed.readableBytes() + 8);
                ByteBuffer inBuffer = compressed.internalNioBuffer(compressed.readerIndex(), compressed.readableBytes());

                this.encryptionCipher.update(inBuffer, outBuffer);
                this.encryptionCipher.update(trailer, outBuffer);
                finalPayload.writerIndex(finalPayload.writerIndex() + compressed.readableBytes() + 8);
            } else {
                finalPayload.writeBytes(compressed);
            }
            this.connection.send(finalPayload);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Unable to encrypt package", e);
        }
    }

    public void tick() {
        this.eventLoop.execute(this::onTick);
    }

    private void onTick() {
        if (this.closed) {
            return;
        }

        this.sendQueued();
    }

    private void sendQueued() {
        BedrockPacket packet;
        List<BedrockPacket> toBatch = new ObjectArrayList<>();
        while ((packet = this.queuedPackets.poll()) != null) {
            if (packet.getClass().isAnnotationPresent(NoEncryption.class)) {
                // We hit a unencryptable packet. Send the current wrapper and then send the unencryptable packet.
                if (!toBatch.isEmpty()) {
                    this.sendWrapped(toBatch, true);
                    toBatch = new ObjectArrayList<>();
                }

                this.sendPacketImmediately(packet);
                continue;
            }

            toBatch.add(packet);
        }

        if (!toBatch.isEmpty()) {
            this.sendWrapped(toBatch, true);
        }
    }

    public synchronized void enableEncryption(@Nonnull SecretKey secretKey) {
        this.checkForClosed();
        log.debug("Encryption enabled.");
        Objects.requireNonNull(secretKey, "secretKey");
        if (!secretKey.getAlgorithm().equals("AES")) {
            throw new IllegalArgumentException("Invalid key algorithm");
        }
        if (this.encryptionCipher != null || this.decryptionCipher != null) {
            throw new IllegalStateException("Encryption has already been enabled");
        }

        this.agreedKey = secretKey;
        boolean useGcm = this.packetCodec.getProtocolVersion() > 428;
        this.encryptionCipher = EncryptionUtils.createCipher(useGcm, true, secretKey);
        this.decryptionCipher = EncryptionUtils.createCipher(useGcm, false, secretKey);
    }

    private byte[] generateTrailer(ByteBuf buf) {
        Sha256 hash = HASH_LOCAL.get();
        ByteBuf counterBuf = ByteBufAllocator.DEFAULT.directBuffer(8);
        try {
            counterBuf.writeLongLE(this.sentEncryptedPacketCount.getAndIncrement());
            ByteBuffer keyBuffer = ByteBuffer.wrap(this.agreedKey.getEncoded());

            hash.update(counterBuf.internalNioBuffer(0, 8));
            hash.update(buf.internalNioBuffer(buf.readerIndex(), buf.readableBytes()));
            hash.update(keyBuffer);
            byte[] digested = hash.digest();
            return Arrays.copyOf(digested, 8);
        } finally {
            counterBuf.release();
            hash.reset();
        }
    }

    public boolean isEncrypted() {
        return encryptionCipher != null;
    }

    public abstract void disconnect();

    void close(DisconnectReason reason) {
        checkForClosed();
        this.closed = true;
        // Free native resources if required
//        if (this.encryptionCipher != null) {
//            this.encryptionCipher.free();
//        }
//        if (this.decryptionCipher != null) {
//            this.decryptionCipher.free();
//        }

        // Destroy secret key
        if (this.agreedKey != null && !this.agreedKey.isDestroyed()) {
            try {
                this.agreedKey.destroy();
            } catch (DestroyFailedException e) {
                // Ignore - throws exception by default
            }
        }
        for (Consumer<DisconnectReason> disconnectHandler : this.disconnectHandlers) {
            disconnectHandler.accept(reason);
        }
    }

    public void onWrappedPacket(final ByteBuf batched) {
        try {
            if (this.isEncrypted()) {
                // This method only supports contiguous buffers, not composite.
                ByteBuffer inBuffer = batched.internalNioBuffer(batched.readerIndex(), batched.readableBytes());
                ByteBuffer outBuffer = inBuffer.duplicate();
                // Copy-safe so we can use the same buffer.
                this.decryptionCipher.update(inBuffer, outBuffer);

                // TODO: Maybe verify the checksum?

                batched.writerIndex(batched.writerIndex() - 8);
            }
            batched.markReaderIndex();

            if (batched.isReadable()) {
                List<BedrockPacket> packets = new ObjectArrayList<>();
                this.wrapperSerializer.deserialize(batched, this.packetCodec, packets, this);
                this.batchHandler.handle(this, batched, packets);
            }
        } catch (GeneralSecurityException ignore) {
        } catch (PacketSerializeException e) {
            log.warn("Error whilst decoding packets", e);
        }
    }

    public InetSocketAddress getAddress() {
        return this.connection.getAddress();
    }

    public InetSocketAddress getRealAddress() {
        return this.connection.getRealAddress();
    }

    public boolean isClosed() {
        return this.connection.isClosed();
    }

    public BedrockPacketCodec getPacketCodec() {
        return this.packetCodec;
    }

    public BedrockPacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    public BatchHandler<BedrockSession, BedrockPacket> getBatchHandler() {
        return this.batchHandler;
    }

    public void setBatchHandler(BatchHandler<BedrockSession, BedrockPacket> batchHandler) {
        this.batchHandler = Objects.requireNonNull(batchHandler, "batchHandler");
    }

    public void setCompressionLevel(int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }

    public int getCompressionLevel() {
        return compressionLevel;
    }

    public boolean isLogging() {
        return this.logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }

    public void addDisconnectHandler(Consumer<DisconnectReason> disconnectHandler) {
        Objects.requireNonNull(disconnectHandler, "disconnectHandler");
        this.disconnectHandlers.add(disconnectHandler);
    }

    public AtomicInteger getHardcodedBlockingId() {
        return this.hardcodedBlockingId;
    }

    @Override
    public long getLatency() {
        return this.connection.getPing();
    }

    public EventLoop getEventLoop() {
        return this.eventLoop;
    }

    public SessionConnection<ByteBuf> getConnection() {
        return this.connection;
    }

//    @ParametersAreNonnullByDefault
//    abstract class BedrockSessionListener implements RakNetSessionListener {
//
//        @Override
//        public void onEncapsulated(EncapsulatedPacket packet) {
//            if (BedrockSession.this.connection.getState() != RakNetState.CONNECTED) {
//                // We shouldn't be receiving packets till the connection is full established.
//                return;
//            }
//            ByteBuf buffer = packet.getBuffer();
//
//            int packetId = buffer.readUnsignedByte();
//            if (packetId == 0xfe /* Wrapper packet */) {
//                BedrockSession.this.onWrappedPacket(buffer);
//            }
//        }
//
//        @Override
//        public void onDirect(ByteBuf buf) {
//            // We shouldn't be receiving direct datagram messages from the client whilst they are connected.
//        }
//    }
}
