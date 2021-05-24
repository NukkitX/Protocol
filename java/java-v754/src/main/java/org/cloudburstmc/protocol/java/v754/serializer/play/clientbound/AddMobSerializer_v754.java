package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.AddMobPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddMobSerializer_v754 implements JavaPacketSerializer<AddMobPacket> {
    public static final AddMobSerializer_v754 INSTANCE = new AddMobSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, AddMobPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getEntityId());
        helper.writeUUID(buffer, packet.getUuid());
        VarInts.writeUnsignedInt(buffer, helper.getEntityId(packet.getEntityType()));
        helper.writePosition(buffer, packet.getPosition());
        helper.writeRotation3f(buffer, packet.getRotation());
        helper.writeVelocity(buffer, packet.getVelocity());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, AddMobPacket packet) {
        packet.setEntityId(VarInts.readUnsignedInt(buffer));
        packet.setUuid(helper.readUUID(buffer));
        packet.setEntityType(helper.getEntityType(VarInts.readUnsignedInt(buffer)));
        packet.setPosition(helper.readPosition(buffer));
        packet.setRotation(helper.readRotation3f(buffer));
        packet.setVelocity(helper.readVelocity(buffer));
    }
}
