package org.cloudburstmc.protocol.java.v754.serializer.status;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.JavaPong;
import org.cloudburstmc.protocol.java.packet.status.StatusResponsePacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatusResponseSerializer_v754 implements JavaPacketSerializer<StatusResponsePacket> {
    public static final StatusResponseSerializer_v754 INSTANCE = new StatusResponseSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, StatusResponsePacket packet) {
        helper.writeString(buffer, GsonComponentSerializer.gson().serializer().toJson(packet.getResponse()));
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, StatusResponsePacket packet) {
        packet.setResponse(GsonComponentSerializer.gson().serializer().fromJson(helper.readString(buffer), JavaPong.class));
    }
}
