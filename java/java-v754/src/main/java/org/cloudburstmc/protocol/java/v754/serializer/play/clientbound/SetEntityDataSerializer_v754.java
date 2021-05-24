package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.entity.EntityData;
import org.cloudburstmc.protocol.java.packet.play.clientbound.SetEntityDataPacket;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SetEntityDataSerializer_v754 implements JavaPacketSerializer<SetEntityDataPacket> {
    public static final SetEntityDataSerializer_v754 INSTANCE = new SetEntityDataSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, SetEntityDataPacket packet) throws PacketSerializeException {
        helper.writeVarInt(buffer, packet.getEntityId());
        for (EntityData<?> data : packet.getEntityData()) {
            buffer.writeByte(data.getId());
            helper.writeVarInt(buffer, helper.getEntityDataTypeId(data.getType()));
            helper.writeEntityData(data, buffer);
        }
        buffer.writeByte(0xff);
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, SetEntityDataPacket packet) throws PacketSerializeException {
        packet.setEntityId(helper.readVarInt(buffer));
        List<EntityData<?>> entityData = new ObjectArrayList<>();
        int dataId;
        while ((dataId = buffer.readUnsignedByte()) != 0xff) {
            int id = helper.readVarInt(buffer);
            entityData.add(helper.readEntityData(dataId, id, buffer));
        }
        packet.setEntityData(entityData);
    }
}
