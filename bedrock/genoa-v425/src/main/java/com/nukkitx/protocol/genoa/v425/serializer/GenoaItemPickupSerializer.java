package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaItemPickupPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaItemPickupSerializer implements BedrockPacketSerializer<GenoaItemPickupPacket> {
    public static final GenoaItemPickupSerializer INSTANCE = new GenoaItemPickupSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaItemPickupPacket packet) {
        helper.writeUuid(buffer,packet.getUuid());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaItemPickupPacket packet) {
        packet.setUuid(helper.readUuid(buffer));
    }
}


