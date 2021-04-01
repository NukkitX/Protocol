package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaGameplaySettings;
import com.nukkitx.protocol.genoa.packet.GenoaNetworkOwnershipStatusPacket;
import io.netty.buffer.ByteBuf;

public class GenoaGameplaySettingsSerializer implements BedrockPacketSerializer<GenoaGameplaySettings> {
    public static final GenoaGameplaySettingsSerializer INSTANCE = new GenoaGameplaySettingsSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaGameplaySettings packet) {
        buffer.writeBoolean(packet.isBool1());
        if(packet.isBool1()) {
            buffer.writeLong(packet.getUnsignedLong());
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaGameplaySettings packet) {
        packet.setBool1(buffer.readBoolean());
        if (packet.isBool1()) {
            packet.setUnsignedLong(buffer.readLong());
        }
    }
}
