package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaGuestPlayerJoinResponsePacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaGuestPlayerJoinResponseSerializer implements BedrockPacketSerializer<GenoaGuestPlayerJoinResponsePacket> {
    public static final GenoaGuestPlayerJoinResponseSerializer INSTANCE = new GenoaGuestPlayerJoinResponseSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaGuestPlayerJoinResponsePacket packet) {
        helper.writeString(buffer,packet.getStringVal());
        buffer.writeBoolean(packet.isBoolVal());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaGuestPlayerJoinResponsePacket packet) {
       packet.setStringVal(helper.readString(buffer));
       packet.setBoolVal(buffer.readBoolean());
    }
}


