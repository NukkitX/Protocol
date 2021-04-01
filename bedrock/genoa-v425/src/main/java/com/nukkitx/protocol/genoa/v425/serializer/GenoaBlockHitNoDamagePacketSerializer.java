package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaBlockHitNoDamagePacket;
import com.nukkitx.protocol.genoa.packet.GenoaGameplaySettings;
import io.netty.buffer.ByteBuf;

public class GenoaBlockHitNoDamagePacketSerializer implements BedrockPacketSerializer<GenoaBlockHitNoDamagePacket> {
    public static final GenoaBlockHitNoDamagePacketSerializer INSTANCE = new GenoaBlockHitNoDamagePacketSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaBlockHitNoDamagePacket packet) {
        helper.writeBlockPosition(buffer,packet.getPosition());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaBlockHitNoDamagePacket packet) {
        packet.setPosition(helper.readBlockPosition(buffer));
    }
}
