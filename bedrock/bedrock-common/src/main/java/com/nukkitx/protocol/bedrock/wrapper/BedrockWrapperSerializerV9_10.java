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

public class BedrockWrapperSerializerV9_10 extends BedrockWrapperSerializer {
    public static final BedrockWrapperSerializerV9_10 V9 = new BedrockWrapperSerializerV9_10(Zlib.DEFAULT);
    public static final BedrockWrapperSerializerV9_10 V10 = new BedrockWrapperSerializerV9_10(Zlib.RAW);

    private final Zlib zlib;

    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    private static final int CHUNK_SIZE = 8192;

    private BedrockWrapperSerializerV9_10(Zlib zlib) {
        this.zlib = zlib;
    }

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketCodec codec, Collection<BedrockPacket> packets, int level) {
        ByteBuf uncompressed = ByteBufAllocator.DEFAULT.ioBuffer(packets.size() << 3);
        try {
            for (BedrockPacket packet : packets) {
                ByteBuf packetBuffer = ByteBufAllocator.DEFAULT.ioBuffer();
                try {
                    int id = codec.getId(packet.getClass());
                    int header = 0;
                    header |= (id & 0x3ff);
                    header |= (packet.getSenderId() & 3) << 10;
                    header |= (packet.getClientId() & 3) << 12;
                    VarInts.writeUnsignedInt(packetBuffer, header);
                    codec.tryEncode(packetBuffer, packet);

                    VarInts.writeUnsignedInt(uncompressed, packetBuffer.readableBytes());
                    uncompressed.writeBytes(packetBuffer);
                } catch (PacketSerializeException e) {
                    log.debug("Error occurred whilst encoding " + packet.getClass().getSimpleName(), e);
                } finally {
                    packetBuffer.release();
                }
            }
            zlib.deflate(uncompressed, buffer, level);
        } catch (DataFormatException e) {
            throw new RuntimeException("Unable to deflate buffer data", e);
        } finally {
            uncompressed.release();
        }
    }

    @Override
    public void deserialize(ByteBuf compressed, BedrockPacketCodec codec, Collection<BedrockPacket> packets) {
        ByteBuf decompressed = ByteBufAllocator.DEFAULT.ioBuffer(BUFFER_SIZE);

        InflateStream stream = zlib.inflateStream(compressed);
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
                            int header = VarInts.readUnsignedInt(packetBuffer);
                            int packetId = header & 0x3ff;
                            BedrockPacket packet = codec.tryDecode(packetBuffer, packetId);
                            packet.setSenderId((header >>> 10) & 3);
                            packet.setClientId((header >>> 12) & 3);
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
