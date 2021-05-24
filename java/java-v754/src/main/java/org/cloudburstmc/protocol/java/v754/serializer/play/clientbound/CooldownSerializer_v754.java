package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.CooldownPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CooldownSerializer_v754 implements JavaPacketSerializer<CooldownPacket> {
    public static final CooldownSerializer_v754 INSTANCE = new CooldownSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, CooldownPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getItemId());
        VarInts.writeUnsignedInt(buffer, packet.getCooldownTicks());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, CooldownPacket packet) {
        packet.setItemId(VarInts.readUnsignedInt(buffer));
        packet.setCooldownTicks(VarInts.readUnsignedInt(buffer));
    }
}
