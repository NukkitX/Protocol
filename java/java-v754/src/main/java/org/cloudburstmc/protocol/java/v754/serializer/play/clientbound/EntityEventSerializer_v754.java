package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.entity.EntityEventType;
import org.cloudburstmc.protocol.java.packet.play.clientbound.EntityEventPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EntityEventSerializer_v754 implements JavaPacketSerializer<EntityEventPacket> {
    public static EntityEventSerializer_v754 INSTANCE = new EntityEventSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, EntityEventPacket packet) {
        buffer.writeInt(packet.getEntityId());
        buffer.writeByte(packet.getEventId().ordinal());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, EntityEventPacket packet) {
        packet.setEntityId(buffer.readInt());
        packet.setEventId(EntityEventType.values()[buffer.readByte()]);
    }
}
