package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.entity.spawn.*;
import org.cloudburstmc.protocol.java.packet.play.clientbound.AddEntityPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddEntitySerializer_v754 implements JavaPacketSerializer<AddEntityPacket> {
    public static final AddEntitySerializer_v754 INSTANCE = new AddEntitySerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, AddEntityPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getEntityId());
        helper.writeUUID(buffer, packet.getUuid());
        VarInts.writeUnsignedInt(buffer, helper.getEntityId(packet.getEntityType()));
        helper.writePosition(buffer, packet.getPosition());
        helper.writeRotation2f(buffer, packet.getRotation());
        buffer.writeInt(packet.getSpawnData().getValue());
        helper.writeVelocity(buffer, packet.getVelocity());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, AddEntityPacket packet) {
        packet.setEntityId(VarInts.readUnsignedInt(buffer));
        packet.setUuid(helper.readUUID(buffer));
        packet.setEntityType(helper.getEntityType(VarInts.readUnsignedInt(buffer)));
        packet.setPosition(helper.readPosition(buffer));
        packet.setRotation(helper.readRotation2f(buffer));

        int data = buffer.readInt();
        switch (packet.getEntityType()) {
            case MINECART:
                packet.setSpawnData(new MinecartSpawnData(data));
                break;
            case ITEM_FRAME:
                packet.setSpawnData(new DirectionSpawnData(data));
                break;
            case FALLING_BLOCK:
                packet.setSpawnData(new FallingBlockSpawnData(data));
                break;
            case ARROW:
            case SPECTRAL_ARROW:
                packet.setSpawnData(new ArrowSpawnData(data));
                break;
            default:
                packet.setSpawnData(new SpawnData(data));
        }
        packet.setVelocity(helper.readVelocity(buffer));
    }
}
