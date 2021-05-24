package org.cloudburstmc.protocol.java.v754.serializer.play.serverbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.serverbound.ClientChatPacket;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientChatSerializer_v754 implements JavaPacketSerializer<ClientChatPacket> {
    public static final ClientChatSerializer_v754 INSTANCE = new ClientChatSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, ClientChatPacket packet) {
        helper.writeString(buffer, packet.getMessage());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, ClientChatPacket packet) {
        packet.setMessage(helper.readString(buffer));
    }
}
