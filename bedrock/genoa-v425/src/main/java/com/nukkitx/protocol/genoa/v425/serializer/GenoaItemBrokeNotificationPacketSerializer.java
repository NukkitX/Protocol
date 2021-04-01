package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaBlockHitNoDamagePacket;
import com.nukkitx.protocol.genoa.packet.GenoaItemBrokeNotificationPacket;
import io.netty.buffer.ByteBuf;

public class GenoaItemBrokeNotificationPacketSerializer implements BedrockPacketSerializer<GenoaItemBrokeNotificationPacket> {
    public static final GenoaItemBrokeNotificationPacketSerializer INSTANCE = new GenoaItemBrokeNotificationPacketSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaItemBrokeNotificationPacket packet) {
        buffer.writeIntLE(packet.getUnsignedInt());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaItemBrokeNotificationPacket packet) {
        packet.setUnsignedInt(buffer.readIntLE());
    }
}
