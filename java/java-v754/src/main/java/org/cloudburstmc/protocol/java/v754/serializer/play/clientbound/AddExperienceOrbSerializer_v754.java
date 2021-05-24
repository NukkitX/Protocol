package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.AddExperienceOrbPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddExperienceOrbSerializer_v754 implements JavaPacketSerializer<AddExperienceOrbPacket> {
    public static final AddExperienceOrbSerializer_v754 INSTANCE = new AddExperienceOrbSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, AddExperienceOrbPacket packet) {
        helper.writeVarInt(buffer, packet.getEntityId());
        helper.writePosition(buffer, packet.getPosition());
        buffer.writeShort(packet.getAmount());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, AddExperienceOrbPacket packet) {
        packet.setEntityId(helper.readVarInt(buffer));
        packet.setPosition(helper.readPosition(buffer));
        packet.setAmount(buffer.readShort());
    }
}
