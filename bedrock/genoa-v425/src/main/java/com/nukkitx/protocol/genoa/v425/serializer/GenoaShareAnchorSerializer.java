package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaShareAnchorPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaShareAnchorSerializer implements BedrockPacketSerializer<GenoaShareAnchorPacket> {
    public static final GenoaShareAnchorSerializer INSTANCE = new GenoaShareAnchorSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaShareAnchorPacket packet) {
        helper.writeVector4f(buffer, packet.getBottomLeft());
        helper.writeVector4f(buffer, packet.getBottomRight());
        helper.writeVector4f(buffer, packet.getTopLeft());
        helper.writeVector4f(buffer, packet.getTopRight());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaShareAnchorPacket packet) {
        packet.setBottomLeft(helper.readVector4f(buffer));
        packet.setBottomRight(helper.readVector4f(buffer));
        packet.setTopLeft(helper.readVector4f(buffer));
        packet.setTopRight(helper.readVector4f(buffer));
    }
}


