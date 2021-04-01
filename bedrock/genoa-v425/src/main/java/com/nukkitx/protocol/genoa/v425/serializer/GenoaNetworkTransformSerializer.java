package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaNetworkTransformPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaNetworkTransformSerializer implements BedrockPacketSerializer<GenoaNetworkTransformPacket> {
    public static final GenoaNetworkTransformSerializer INSTANCE = new GenoaNetworkTransformSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaNetworkTransformPacket packet) {
        buffer.writeByte(packet.getByte1());
        helper.writeVector4f(buffer, packet.getBottomLeft());
        helper.writeVector4f(buffer, packet.getBottomRight());
        helper.writeVector4f(buffer, packet.getTopLeft());
        helper.writeVector4f(buffer, packet.getTopRight());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaNetworkTransformPacket packet) {
        packet.setByte1(buffer.readByte());
        packet.setBottomLeft(helper.readVector4f(buffer));
        packet.setBottomRight(helper.readVector4f(buffer));
        packet.setTopLeft(helper.readVector4f(buffer));
        packet.setTopRight(helper.readVector4f(buffer));

    }
}


