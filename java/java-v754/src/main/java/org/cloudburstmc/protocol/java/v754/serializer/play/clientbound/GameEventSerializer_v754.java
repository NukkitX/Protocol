package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.GameEvent;
import org.cloudburstmc.protocol.java.packet.play.clientbound.GameEventPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GameEventSerializer_v754 implements JavaPacketSerializer<GameEventPacket> {
    public static final GameEventSerializer_v754 INSTANCE = new GameEventSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, GameEventPacket packet) throws PacketSerializeException {
        buffer.writeByte(packet.getEvent().ordinal());
        buffer.writeFloat(packet.getParam());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, GameEventPacket packet) throws PacketSerializeException {
        packet.setEvent(GameEvent.getById(buffer.readUnsignedByte()));
        packet.setParam(buffer.readFloat());
    }
}
