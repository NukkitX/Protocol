package org.cloudburstmc.protocol.java.v754.serializer.play;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.SetCarriedItemPacket;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SetCarriedItemSerializer_v754 implements JavaPacketSerializer<SetCarriedItemPacket> {
    public static final SetCarriedItemSerializer_v754 INSTANCE = new SetCarriedItemSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, SetCarriedItemPacket packet)
            throws PacketSerializeException {
        buffer.writeShort(packet.getSlot());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, SetCarriedItemPacket packet)
            throws PacketSerializeException {
        packet.setSlot(buffer.readShort());
    }
}
