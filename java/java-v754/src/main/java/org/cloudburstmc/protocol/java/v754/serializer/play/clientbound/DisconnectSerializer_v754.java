package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.DisconnectPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DisconnectSerializer_v754 implements JavaPacketSerializer<DisconnectPacket> {
    public static final DisconnectSerializer_v754 INSTANCE = new DisconnectSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, DisconnectPacket packet) {
        helper.writeString(buffer, GsonComponentSerializer.gson().serialize(packet.getReason()));
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, DisconnectPacket packet) {
        packet.setReason(GsonComponentSerializer.gson().deserialize(helper.readString(buffer)));
    }
}
