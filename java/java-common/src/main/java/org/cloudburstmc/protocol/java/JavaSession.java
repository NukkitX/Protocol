package org.cloudburstmc.protocol.java;

import com.google.common.base.Preconditions;
import com.nukkitx.natives.aes.AesFactory;
import com.nukkitx.natives.util.Natives;
import com.nukkitx.network.util.DisconnectReason;
import com.nukkitx.protocol.MinecraftSession;
import io.netty.channel.*;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;
import org.cloudburstmc.protocol.java.handler.JavaPacketHandler;
import org.cloudburstmc.protocol.java.packet.State;
import org.cloudburstmc.protocol.java.packet.play.clientbound.DisconnectPacket;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.pipeline.PacketCompressor;
import org.cloudburstmc.protocol.java.pipeline.PacketDecompressor;
import org.cloudburstmc.protocol.java.pipeline.PacketDecrypter;
import org.cloudburstmc.protocol.java.pipeline.PacketEncrypter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.security.auth.DestroyFailedException;
import java.net.InetSocketAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

@Getter
public abstract class JavaSession extends SimpleChannelInboundHandler<JavaPacket<?>> implements MinecraftSession<JavaPacket<?>> {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(JavaSession.class);
    private static final AesFactory AES_FACTORY = Natives.AES_CFB8.get();

    private final Set<Consumer<DisconnectReason>> disconnectHandlers = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Queue<JavaPacket<?>> queuedPackets = PlatformDependent.newMpscQueue();
    private final EventLoop eventLoop;
    protected final InetSocketAddress address;
    protected JavaPacketCodec packetCodec = null;
    protected Channel channel;
    private SecretKey agreedKey;
    private Cipher encryptionCipher = null;
    private Cipher decryptionCipher = null;
    private JavaPacketHandler packetHandler;
    private State protocolState = State.HANDSHAKING;
    private int compressionThreshold = 256;

    protected long lastKeepAlive;
    @Getter(AccessLevel.NONE)
    private boolean outgoingClientbound;

    private volatile boolean closed = false;
    private volatile boolean logging = true;

    @Setter protected GameProfile profile;

    JavaSession(InetSocketAddress address, Channel channel, EventLoop eventLoop) {
        this.address = address;
        this.channel = channel;
        this.eventLoop = eventLoop;
        outgoingClientbound = this instanceof JavaServerSession;
    }

    public void setPacketCodec(JavaPacketCodec packetCodec) {
        this.packetCodec = packetCodec;
    }

    public void setPacketHandler(@Nonnull JavaPacketHandler packetHandler) {
        this.packetHandler = packetHandler;
    }

    public void setProtocolState(@Nonnull State protocolState) {
        this.protocolState = protocolState;
    }

    void checkForClosed() {
        if (this.closed || this.channel == null || !this.channel.isOpen()) {
            throw new IllegalStateException("Connection has been closed");
        }
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    public boolean isConnected() {
        return !this.closed && this.channel.isOpen();
    }

    public void close() {
        this.closed = true;
        if (this.channel.isOpen()) {
            this.channel.close();
        }
        if (this.agreedKey != null && !this.agreedKey.isDestroyed()) {
            try {
                this.agreedKey.destroy();
            } catch (DestroyFailedException e) {
                // Ignore - throws exception by default
            }
        }
    }

    @Override
    public InetSocketAddress getAddress() {
        return this.address;
    }

    @Override
    public void sendPacket(@Nonnull JavaPacket<?> packet) {
        this.checkPacket(packet);

        // Only queue if player is in the play state
        if (this.protocolState == State.PLAY) {
            this.queuedPackets.add(packet);
        } else {
            this.sendPacketImmediately(packet);
        }
    }

    @Override
    public void sendPacketImmediately(@Nonnull JavaPacket<?> packet) {
        this.checkPacket(packet);
        this.channel.writeAndFlush(packet).addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                exceptionCaught(null, future.cause());
            }
        });
    }

    private void checkPacket(JavaPacket<?> packet) {
        this.checkForClosed();
        Objects.requireNonNull(packet, "packet");

        Preconditions.checkState(this.protocolState == packet.getPacketType().getState(),
                "Expected protocol state %s, but %s was of state %s",
                this.protocolState, packet.toString(), packet.getPacketType().getState());

        if (log.isTraceEnabled() && this.logging) {
            String to = this.getAddress().toString();
            log.trace("Outbound {}: {}", to, packet);
        }

        // Validate direction
        JavaPacketType.Direction direction = packet.getPacketType().getDirection();
        if (outgoingClientbound && direction == JavaPacketType.Direction.SERVERBOUND ||
                !outgoingClientbound && direction == JavaPacketType.Direction.CLIENTBOUND) {
            throw new IllegalArgumentException(
                    "Cannot send a " + direction.name().toLowerCase(Locale.ROOT));
        }

        // Verify that the packet ID exists.
        this.packetCodec.getCodec(this.protocolState).getId(packet, outgoingClientbound);
    }

    @Override
    public long getLatency() {
        return 0;
    }

    public void tick() {
        this.eventLoop.execute(this::onTick);
    }

    private void onTick() {
        if (this.closed) {
            return;
        }
        //todo check if player should timeout

        this.sendQueued();
    }

    private void sendQueued() {
        JavaPacket<?> packet;
        while ((packet = this.queuedPackets.poll()) != null) {
            this.sendPacketImmediately(packet);
        }
    }

    @Override
    public void disconnect() {
        this.disconnect(null, true);
    }

    public void disconnect(@Nullable String reason) {
        this.disconnect(reason, false);
    }

    public void disconnect(@Nullable String reason, boolean hideReason) {
        if (this.protocolState == State.PLAY && this.channel.isOpen()) {
            DisconnectPacket packet = new DisconnectPacket();
            if (reason == null || hideReason) {
                reason = "disconnect.disconnected";
            }
            packet.setReason(Component.text(reason));
            this.sendPacketImmediately(packet);
        }
        this.close();
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
        try {
            this.encryptionCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            this.decryptionCipher = Cipher.getInstance("AES/CFB8/NoPadding");

            this.encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(secretKey.getEncoded()));
            this.decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(secretKey.getEncoded()));

            this.channel.pipeline().addBefore("prepender", "encrypt", new PacketEncrypter(this.encryptionCipher));
            this.channel.pipeline().addBefore("splitter", "decrypt", new PacketDecrypter(this.decryptionCipher));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException ex) {
            throw new RuntimeException("Failed to enable encryption!", ex);
        }
    }

    public void setCompressionThreshold(int compressionThreshold) {
        this.compressionThreshold = compressionThreshold;

        if (this.compressionThreshold < 0) {
            if (this.channel.pipeline().get("compress") != null) {
                this.channel.pipeline().remove("compress");
            }
            if (this.channel.pipeline().get("decompress") != null) {
                this.channel.pipeline().remove("decompress");
            }
        }
        if (this.channel.pipeline().get("compress") == null) {
            this.channel.pipeline().addBefore("encoder", "compress", new PacketCompressor(this));
        }
        if (this.channel.pipeline().get("decompress") == null) {
            this.channel.pipeline().addBefore("decoder", "decompress", new PacketDecompressor(this));
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, JavaPacket<?> javaPacket) throws Exception {
        if (this.channel != null && this.channel.isOpen()) {
            try {
                ((JavaPacket) javaPacket).handle(this.packetHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw ex;
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.disconnect("disconnect.endOfStream");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof TimeoutException) {
            this.disconnect("disconnect.timeout");
        } else {
            this.disconnect("disconnect.genericReason");
        }
        log.warn(cause.getMessage(), cause);
    }
}
