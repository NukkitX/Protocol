package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.inventory.ItemStack;
import org.cloudburstmc.protocol.java.packet.play.clientbound.ContainerSetContentPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContainerSetContentSerializer_v754 implements JavaPacketSerializer<ContainerSetContentPacket> {
    public static final ContainerSetContentSerializer_v754 INSTANCE = new ContainerSetContentSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, ContainerSetContentPacket packet) {
        buffer.writeByte(packet.getContainerId());
        buffer.writeShort(packet.getItems().length);
        for (ItemStack item : packet.getItems()) {
            helper.writeItemStack(buffer, item);
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, ContainerSetContentPacket packet) {
        packet.setContainerId(buffer.readUnsignedByte());
        packet.setItems(new ItemStack[buffer.readShort()]);
        ItemStack[] items = packet.getItems();
        for (int index = 0; index < packet.getItems().length; index++) {
            items[index] = helper.readItemStack(buffer);
            packet.setItems(items);
        }
    }
}
