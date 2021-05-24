package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.ContainerSetSlotPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContainerSetSlotSerializer_v754 implements JavaPacketSerializer<ContainerSetSlotPacket> {
    public static final ContainerSetSlotSerializer_v754 INSTANCE = new ContainerSetSlotSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, ContainerSetSlotPacket packet) {
        buffer.writeByte(packet.getContainerId());
        buffer.writeShort(packet.getSlot());
        helper.writeItemStack(buffer, packet.getItem());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, ContainerSetSlotPacket packet) {
        packet.setContainerId(buffer.readUnsignedByte());
        packet.setSlot(buffer.readShort());
        packet.setItem(helper.readItemStack(buffer));
    }
}
