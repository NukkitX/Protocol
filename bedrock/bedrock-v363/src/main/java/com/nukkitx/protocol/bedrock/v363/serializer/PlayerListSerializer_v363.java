package com.nukkitx.protocol.bedrock.v363.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.PlayerListPacket;
import com.nukkitx.protocol.bedrock.v291.serializer.PlayerListSerializer_v291;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.nukkitx.protocol.bedrock.packet.PlayerListPacket.Action;
import static com.nukkitx.protocol.bedrock.packet.PlayerListPacket.Entry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerListSerializer_v363 extends PlayerListSerializer_v291 {
    public static final PlayerListSerializer_v363 INSTANCE = new PlayerListSerializer_v363();


    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, PlayerListPacket packet) {
        buffer.writeByte(packet.getAction().ordinal());
        VarInts.writeUnsignedInt(buffer, packet.getEntries().size());

        for (Entry entry : packet.getEntries()) {
            helper.writeUuid(buffer, entry.getUuid());

            if (packet.getAction() == Action.ADD) {
                VarInts.writeLong(buffer, entry.getEntityId());
                helper.writeString(buffer, entry.getName());
                SerializedSkin skin = entry.getSkin();
                helper.writeString(buffer, skin.getSkinId());
                skin.getSkinData().checkLegacySkinSize();
                helper.writeByteArray(buffer, skin.getSkinData().getImage());
                skin.getCapeData().checkLegacyCapeSize();
                helper.writeByteArray(buffer, skin.getCapeData().getImage());
                helper.writeString(buffer, skin.getGeometryName());
                helper.writeString(buffer, skin.getGeometryData());
                helper.writeString(buffer, entry.getXuid());
                helper.writeString(buffer, entry.getPlatformChatId());
                buffer.writeBoolean(entry.isTeacher());
                buffer.writeBoolean(entry.isHost());
            }
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, PlayerListPacket packet) {
        Action action = Action.values()[buffer.readUnsignedByte()];
        packet.setAction(action);
        int length = VarInts.readUnsignedInt(buffer);

        for (int i = 0; i < length; i++) {
            Entry entry = new Entry(helper.readUuid(buffer));

            if (action == Action.ADD) {
                entry.setEntityId(VarInts.readLong(buffer));
                entry.setName(helper.readString(buffer));
                String skinId = helper.readString(buffer);
                ImageData skinData = ImageData.of(helper.readByteArray(buffer));
                ImageData capeData = ImageData.of(64, 32, helper.readByteArray(buffer));
                String geometryName = helper.readString(buffer);
                String geometryData = helper.readString(buffer);
                entry.setSkin(SerializedSkin.of(skinId, skinData, capeData, geometryName, geometryData, false));
                entry.setXuid(helper.readString(buffer));
                entry.setPlatformChatId(helper.readString(buffer));

                entry.setTeacher(buffer.readBoolean());
                entry.setHost(buffer.readBoolean());
            }
            packet.getEntries().add(entry);
        }
    }
}
