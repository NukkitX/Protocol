package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaThirdPersonItemParticlePacket;
import io.netty.buffer.ByteBuf;

public class GenoaThirdPersonItemParticlePacketSerializer implements BedrockPacketSerializer<GenoaThirdPersonItemParticlePacket> {
    public static final GenoaThirdPersonItemParticlePacketSerializer INSTANCE = new GenoaThirdPersonItemParticlePacketSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaThirdPersonItemParticlePacket packet) {
        VarInts.writeInt(buffer, packet.getParticleId());
        VarInts.writeInt(buffer, packet.getDimensionId());
        helper.writeVector3f(buffer, packet.getPosition());
        helper.writeVector3f(buffer, packet.getViewDirection());
        VarInts.writeUnsignedLong(buffer, packet.getUnsignedVarLong1());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaThirdPersonItemParticlePacket packet) {
        packet.setParticleId(VarInts.readInt(buffer));
        packet.setDimensionId(VarInts.readInt(buffer));
        packet.setPosition(helper.readVector3f(buffer));
        packet.setViewDirection(helper.readVector3f(buffer));
        packet.setUnsignedVarLong1((VarInts.readUnsignedLong(buffer)));
    }
}
