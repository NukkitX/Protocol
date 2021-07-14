package com.nukkitx.protocol.bedrock.wrapper;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.exception.PacketSerializeException;
import com.nukkitx.protocol.util.Zlib;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

import java.util.Collection;
import java.util.zip.DataFormatException;

public class BedrockWrapperSerializerV7 extends BedrockWrapperSerializer {
    public static final BedrockWrapperSerializerV7 INSTANCE = new BedrockWrapperSerializerV7();

    private static final Zlib ZLIB = Zlib.DEFAULT;

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketCodec codec, Collection<BedrockPacket> packets, int level, BedrockSession session) {
        ByteBuf uncompressed = ByteBufAllocator.DEFAULT.ioBuffer(packets.size() << 3);
        try {
            for (BedrockPacket packet : packets) {
                ByteBuf packetBuffer = ByteBufAllocator.DEFAULT.ioBuffer();
                try {
                    int id = codec.getId(packet);
                    packetBuffer.writeByte(id);
                    codec.tryEncode(packetBuffer, packet, session);

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
    public void deserialize(ByteBuf compressed, BedrockPacketCodec codec, Collection<BedrockPacket> packets, BedrockSession session) {
        ByteBuf decompressed = ByteBufAllocator.DEFAULT.ioBuffer();
        try {
            ZLIB.inflate(compressed, decompressed, 2 * 1024 * 1024); // 2MBs

            while (decompressed.isReadable()) {
                int length = VarInts.readUnsignedInt(decompressed);
                ByteBuf packetBuffer = decompressed.readSlice(length);

                if (!packetBuffer.isReadable()) {
                    throw new DataFormatException("Packet cannot be empty");
                }

                try {
                    int packetId = packetBuffer.readUnsignedByte();
                    BedrockPacket packet = codec.tryDecode(packetBuffer, packetId, session);
                    packet.setPacketId(packetId);
                    packets.add(packet);
                } catch (PacketSerializeException e) {
                    log.debug("Error occurred whilst decoding packet", e);
                    if (log.isTraceEnabled()) {
                        log.trace("Packet contents\n{}", ByteBufUtil.prettyHexDump(packetBuffer.readerIndex(0)));
                    }
                }
            }
        } catch (DataFormatException e) {
            throw new RuntimeException("Unable to inflate buffer data", e);
        } finally {
            decompressed.release();
        }
    }
}