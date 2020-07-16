package com.nukkitx.protocol.bedrock.wrapper;

import java.util.Collection;
import java.util.zip.DataFormatException;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.exception.PacketSerializeException;
import com.nukkitx.protocol.util.InflateStream;
import com.nukkitx.protocol.util.Zlib;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BedrockWrapperSerializerV8 extends BedrockWrapperSerializer {
    public static final BedrockWrapperSerializerV8 INSTANCE = new BedrockWrapperSerializerV8();

    private static final Zlib ZLIB = Zlib.DEFAULT;

    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    private static final int CHUNK_SIZE = 8192;

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketCodec codec, Collection<BedrockPacket> packets, int level) {
        ByteBuf uncompressed = ByteBufAllocator.DEFAULT.ioBuffer(packets.size() << 3);
        try {
            for (BedrockPacket packet : packets) {
                ByteBuf packetBuffer = ByteBufAllocator.DEFAULT.ioBuffer();
                try {
                    int id = codec.getId(packet.getClass());
                    packetBuffer.writeByte(id);
                    packetBuffer.writeByte(packet.getSenderId());
                    packetBuffer.writeByte(packet.getClientId());
                    codec.tryEncode(packetBuffer, packet);

                    VarInts.writeUnsignedInt(uncompressed, packetBuffer.readableBytes());
                    uncompressed.writeBytes(packetBuffer);
                } catch (PacketSerializeException e) {
                    log.debug("Error occurred whilst encoding " + packet.getClass().getSimpleName(), e);
                } finally {
                    packetBuffer.release();
                }
            }
            ZLIB.deflate(uncompressed, buffer, level);
        } catch (DataFormatException e) {
            throw new RuntimeException("Unable to deflate buffer data", e);
        } finally {
            uncompressed.release();
        }
    }

    @Override
    public void deserialize(ByteBuf compressed, BedrockPacketCodec codec, Collection<BedrockPacket> packets) {
        ByteBuf decompressed = ByteBufAllocator.DEFAULT.ioBuffer(BUFFER_SIZE);

        InflateStream stream = ZLIB.inflateStream(compressed);
        try {
            while (!stream.finished()) {
                // Inflate initial chunk
                int read = stream.inflateChunk(decompressed, CHUNK_SIZE);
                log.trace("Read {} bytes. Current buffer: {}", read, decompressed);

                try {
                    // Store reader index for if we don't have enough bytes
                    // to get the length of the next packet
                    decompressed.markReaderIndex();
                    int length = VarInts.readUnsignedInt(decompressed);

                    // Inflate enough bytes for the next packet or at least CHUNK_SIZE bytes
                    while (decompressed.readableBytes() < length) {
                        read = stream.inflateChunk(decompressed, Math.max(CHUNK_SIZE, length - decompressed.readableBytes()));
                        log.trace("Read {} bytes. Current buffer: {}. {} bytes needed for packet.", read, decompressed, length);
                    }

                    // We should have enough bytes, but just in case
                    if (decompressed.readableBytes() >= length) {
                        ByteBuf packetBuffer = decompressed.readSlice(length);

                        if (!packetBuffer.isReadable()) {
                            throw new DataFormatException("Packet cannot be empty");
                        }

                        try {
                            int packetId = packetBuffer.readUnsignedByte();
                            BedrockPacket packet = codec.tryDecode(packetBuffer, packetId);
                            packet.setSenderId(packetBuffer.readUnsignedByte());
                            packet.setClientId(packetBuffer.readUnsignedByte());
                            packets.add(packet);
                        } catch (PacketSerializeException e) {
                            log.debug("Error occurred whilst decoding packet", e);
                            log.trace("Packet contents\n{}", ByteBufUtil.prettyHexDump(packetBuffer.readerIndex(0)));
                        }
                    } else {
                        // We need to read the length bytes again in the next loop iteration
                        decompressed.resetReaderIndex();
                    }
                } catch (ArithmeticException e) {
                    log.debug("Failed to read length: {}", e.getMessage(), e);
                    // We need to read the length bytes again in the next loop iteration
                    decompressed.resetReaderIndex();
                }

                // Read bytes have been decoded into packets and can be discarded
                // freeing up space in the buffer. We call this late to minimize overhead
                decompressed.discardReadBytes();
            }
        } catch (DataFormatException e) {
            throw new RuntimeException("Unable to inflate buffer data", e);
        } finally {
            // We need to free the stream to release the source buffer & free the inflater
            stream.free();

            if (decompressed != null) {
                decompressed.release();
            }
        }
    }
}
