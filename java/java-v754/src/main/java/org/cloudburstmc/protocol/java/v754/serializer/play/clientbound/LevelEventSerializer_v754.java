package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.LevelEventType;
import org.cloudburstmc.protocol.java.packet.play.clientbound.LevelEventPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelEventSerializer_v754 implements JavaPacketSerializer<LevelEventPacket> {
    public static final LevelEventSerializer_v754 INSTANCE = new LevelEventSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, LevelEventPacket packet) throws PacketSerializeException {
        buffer.writeInt(packet.getType().ordinal());
        helper.writeBlockPosition(buffer, packet.getPosition());
        buffer.writeInt(packet.getData());
        buffer.writeBoolean(packet.isGlobalEvent());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, LevelEventPacket packet) throws PacketSerializeException {
        packet.setType(LevelEventType.getById(buffer.readInt()));
        packet.setPosition(helper.readBlockPosition(buffer));
        packet.setData(buffer.readInt());
        packet.setGlobalEvent(buffer.readBoolean());
    }
}
