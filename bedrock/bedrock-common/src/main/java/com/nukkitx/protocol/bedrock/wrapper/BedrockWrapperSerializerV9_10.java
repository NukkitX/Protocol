package com.nukkitx.protocol.bedrock.wrapper;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketCodec;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.exception.PacketSerializeException;
import com.nukkitx.protocol.util.Zlib;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;

import java.util.Collection;
import java.util.zip.DataFormatException;

public class BedrockWrapperSerializerV9_10 extends BedrockWrapperSerializer {
    public static final BedrockWrapperSerializerV9_10 V9 = new BedrockWrapperSerializerV9_10(Zlib.DEFAULT);
    public static final BedrockWrapperSerializerV9_10 V10 = new BedrockWrapperSerializerV9_10(Zlib.RAW);

    private final Zlib zlib;

    private BedrockWrapperSerializerV9_10(Zlib zlib) {
        this.zlib = zlib;
    }

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketCodec codec, Collection<BedrockPacket> packets, int level, BedrockSession session) {
        ByteBuf uncompressed = ByteBufAllocator.DEFAULT.ioBuffer(packets.size() << 3);
        try {
            for (BedrockPacket packet : packets) {
                ByteBuf packetBuffer = ByteBufAllocator.DEFAULT.ioBuffer();
                try {
                    int id = codec.getId(packet);
                    int header = 0;
                    header |= (id & 0x3ff);
                    header |= (packet.getSenderId() & 3) << 10;
                    header |= (packet.getClientId() & 3) << 12;
                    VarInts.writeUnsignedInt(packetBuffer, header);
                    codec.tryEncode(packetBuffer, packet, session);

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
    public void deserialize(ByteBuf compressed, BedrockPacketCodec codec, Collection<BedrockPacket> packets, BedrockSession session) {
        ByteBuf decompressed = null;
        try {
            decompressed = zlib.inflate(compressed, 12 * 1024 * 1024); // 12MBs

            while (decompressed.isReadable()) {
                int length = VarInts.readUnsignedInt(decompressed);
                ByteBuf packetBuffer = decompressed.readSlice(length);

                if (!packetBuffer.isReadable()) {
                    throw new DataFormatException("Packet cannot be empty");
                }

                try {
                    int header = VarInts.readUnsignedInt(packetBuffer);
                    int packetId = header & 0x3ff;
                    BedrockPacket packet = codec.tryDecode(packetBuffer, packetId, session);
                    packet.setPacketId(packetId);
                    packet.setSenderId((header >>> 10) & 3);
                    packet.setClientId((header >>> 12) & 3);
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
            if (decompressed != null) {
                decompressed.release();
            }
        }
    }
}
