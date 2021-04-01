package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.data.ItemAwardedNotification;
import com.nukkitx.protocol.genoa.packet.GenoaItemAwardedNotificationPacket;
import io.netty.buffer.ByteBuf;

import java.util.UUID;

public class GenoaItemAwardedNotificationPacketSerializer implements BedrockPacketSerializer<GenoaItemAwardedNotificationPacket> {
    public static final GenoaItemAwardedNotificationPacketSerializer INSTANCE = new GenoaItemAwardedNotificationPacketSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaItemAwardedNotificationPacket packet) {
        helper.writeString(buffer,packet.getString1());
        helper.writeString(buffer,packet.getString2());
        helper.writeString(buffer,packet.getString3());
        helper.writeArray(buffer, packet.getArr(), (buf,help,pac) -> {
            buffer.writeIntLE(pac.getRuntimeEntityId());
            buffer.writeIntLE(pac.getAmount());
            helper.writeUuid(buffer,pac.getCatalogItemUuid());
        });
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaItemAwardedNotificationPacket packet) {
        packet.setString1(helper.readString(buffer));
        packet.setString2(helper.readString(buffer));
        packet.setString3(helper.readString(buffer));
        helper.readArray(buffer,packet.getArr(), (buf,help) -> {
            int UnsignedInt = buffer.readIntLE();
            int SignedInt = buffer.readIntLE();
            UUID Uuid = helper.readUuid(buffer);
            return new ItemAwardedNotification(UnsignedInt,SignedInt,Uuid);
        });

    }
}
