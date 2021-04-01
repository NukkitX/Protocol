package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaGuestPlayerJoinRequestPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaGuestPlayerJoinRequestSerializer implements BedrockPacketSerializer<GenoaGuestPlayerJoinRequestPacket> {
    public static final GenoaGuestPlayerJoinRequestSerializer INSTANCE = new GenoaGuestPlayerJoinRequestSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaGuestPlayerJoinRequestPacket packet) {
        helper.writeString(buffer,packet.getS1());
        buffer.writeBoolean(packet.isB1());
        if (packet.isB1()) {
            helper.writeString(buffer,packet.getS2());
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaGuestPlayerJoinRequestPacket packet) {
        packet.setS1(helper.readString(buffer));
        packet.setB1(buffer.readBoolean());
        if (packet.isB1()) {
            packet.setS2(helper.readString(buffer));
        }
    }
}


