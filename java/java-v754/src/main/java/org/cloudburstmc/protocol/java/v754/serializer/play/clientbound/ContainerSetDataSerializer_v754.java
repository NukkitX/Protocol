package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.ContainerSetDataPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContainerSetDataSerializer_v754 implements JavaPacketSerializer<ContainerSetDataPacket> {
    public static final ContainerSetDataSerializer_v754 INSTANCE = new ContainerSetDataSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, ContainerSetDataPacket packet) {
        buffer.writeByte(packet.getContainerId());
        buffer.writeShort(packet.getProperty());
        buffer.writeShort(packet.getValue());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, ContainerSetDataPacket packet) {
        packet.setContainerId(buffer.readUnsignedByte());
        packet.setProperty(buffer.readShort());
        packet.setValue(buffer.readShort());
    }
}
