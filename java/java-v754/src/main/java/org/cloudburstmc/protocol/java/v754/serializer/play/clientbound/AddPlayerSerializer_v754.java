package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.AddPlayerPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddPlayerSerializer_v754 implements JavaPacketSerializer<AddPlayerPacket> {
    public static final AddPlayerSerializer_v754 INSTANCE = new AddPlayerSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, AddPlayerPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getEntityId());
        helper.writeUUID(buffer, packet.getUuid());
        helper.writePosition(buffer, packet.getPosition());
        helper.writeRotation2f(buffer, packet.getRotation());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, AddPlayerPacket packet) {
        packet.setEntityId(VarInts.readUnsignedInt(buffer));
        packet.setUuid(helper.readUUID(buffer));
        packet.setPosition(helper.readPosition(buffer));
        packet.setRotation(helper.readRotation2f(buffer));
    }
}
