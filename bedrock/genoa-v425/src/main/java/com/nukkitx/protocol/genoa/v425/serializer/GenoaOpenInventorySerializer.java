package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaOpenInventoryPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaOpenInventorySerializer implements BedrockPacketSerializer<GenoaOpenInventoryPacket> {
    public static final GenoaOpenInventorySerializer INSTANCE = new GenoaOpenInventorySerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaOpenInventoryPacket packet) {
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaOpenInventoryPacket packet) {
    }
}


