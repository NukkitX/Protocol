package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.text.ChatPosition;
import org.cloudburstmc.protocol.java.packet.play.clientbound.ServerChatPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerChatSerializer_v754 implements JavaPacketSerializer<ServerChatPacket> {
    public static final ServerChatSerializer_v754 INSTANCE = new ServerChatSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, ServerChatPacket packet) {
        helper.writeString(buffer, GsonComponentSerializer.gson().serialize(packet.getMessage()));
        buffer.writeByte(packet.getPosition().ordinal());
        helper.writeUUID(buffer, packet.getSenderUuid());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, ServerChatPacket packet) {
        packet.setMessage(GsonComponentSerializer.gson().deserialize(helper.readString(buffer)));
        packet.setPosition(ChatPosition.getById(buffer.readByte()));
        packet.setSenderUuid(helper.readUUID(buffer));
    }
}
