package org.cloudburstmc.protocol.java.v754.serializer.play;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.ContainerClosePacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContainerCloseSerializer_v754 implements JavaPacketSerializer<ContainerClosePacket> {
    public static final ContainerCloseSerializer_v754 INSTANCE = new ContainerCloseSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, ContainerClosePacket packet) throws PacketSerializeException {
        buffer.writeByte(packet.getContainerId());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, ContainerClosePacket packet) throws PacketSerializeException {
        packet.setContainerId(buffer.readUnsignedByte());
    }
}
