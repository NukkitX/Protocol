package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.HorseScreenOpenPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HorseScreenOpenSerializer_v754  implements JavaPacketSerializer<HorseScreenOpenPacket> {
    public static final HorseScreenOpenSerializer_v754 INSTANCE = new HorseScreenOpenSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, HorseScreenOpenPacket packet) throws PacketSerializeException {
        buffer.writeByte(packet.getContainerId());
        VarInts.writeUnsignedInt(buffer, packet.getSize());
        buffer.writeInt(packet.getEntityId());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, HorseScreenOpenPacket packet) throws PacketSerializeException {
        packet.setContainerId(buffer.readUnsignedByte());
        packet.setSize(VarInts.readUnsignedInt(buffer));
        packet.setEntityId(buffer.readInt());
    }
}
