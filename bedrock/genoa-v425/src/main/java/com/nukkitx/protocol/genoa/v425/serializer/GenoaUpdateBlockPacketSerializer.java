package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.exception.PacketSerializeException;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import com.nukkitx.protocol.genoa.packet.GenoaUpdateBlockPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaUpdateBlockPacketSerializer implements BedrockPacketSerializer<GenoaUpdateBlockPacket> {
    public static final GenoaUpdateBlockPacketSerializer INSTANCE = new GenoaUpdateBlockPacketSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaUpdateBlockPacket packet) {
        helper.writeBlockPosition(buffer, packet.getBlockPosition()); // 32, 36, 44
        VarInts.writeUnsignedInt(buffer, packet.getRuntimeId()); // 64
        int flagValue = 0;
        for (UpdateBlockPacket.Flag flag : packet.getFlags()) {
            flagValue |= (1 << flag.ordinal());
        }
        VarInts.writeUnsignedInt(buffer, flagValue); // 48

        VarInts.writeUnsignedInt(buffer,packet.getDataLayer()); // 44

        if (packet.isCheckForFloat()) {
            buffer.writeFloat(packet.getFloat1()); // 56
            buffer.writeBoolean(true); // 60
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaUpdateBlockPacket packet) {

        packet.setBlockPosition(helper.readBlockPosition(buffer));
        packet.setRuntimeId(VarInts.readUnsignedInt(buffer));

        int flagValue = VarInts.readUnsignedInt(buffer);

        Set<UpdateBlockPacket.Flag> flags = packet.getFlags();
        for (UpdateBlockPacket.Flag flag : UpdateBlockPacket.Flag.values()) {
            if ((flagValue & (1 << flag.ordinal())) != 0) {
                flags.add(flag);
            }
        }

        packet.setDataLayer(VarInts.readUnsignedInt(buffer));

        try {
            float ftemp = buffer.readFloatLE();
            boolean temp = buffer.readBoolean();
            if (!temp) {
            } else {
                packet.setFloat1(ftemp);
                packet.setCheckForFloat(true);
            }
        } catch (Exception e) {
            throw new PacketSerializeException(e);
        }
    }
}
