package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaPlayerHurtPacket;
import io.netty.buffer.ByteBuf;

public class GenoaPlayerHurtPacketSerializer implements BedrockPacketSerializer<GenoaPlayerHurtPacket> {
    public static final GenoaPlayerHurtPacketSerializer INSTANCE = new GenoaPlayerHurtPacketSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaPlayerHurtPacket packet) {
        buffer.writeIntLE(packet.getInt1());
        buffer.writeIntLE(packet.getInt2());
        buffer.writeFloatLE(packet.getFloat1());
        buffer.writeFloatLE(packet.getFloat2());
        buffer.writeFloatLE(packet.getFloat3());
        buffer.writeFloatLE(packet.getFloat4());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaPlayerHurtPacket packet) {
        packet.setInt1(buffer.readIntLE());
        packet.setInt2(buffer.readIntLE());
        packet.setFloat1(buffer.readFloatLE());
        packet.setFloat2(buffer.readFloatLE());
        packet.setFloat3(buffer.readFloatLE());
        packet.setFloat4(buffer.readFloatLE());
    }
}
