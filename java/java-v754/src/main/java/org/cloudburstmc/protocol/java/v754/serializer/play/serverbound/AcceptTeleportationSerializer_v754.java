package org.cloudburstmc.protocol.java.v754.serializer.play.serverbound;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.serverbound.AcceptTeleportationPacket;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AcceptTeleportationSerializer_v754 implements JavaPacketSerializer<AcceptTeleportationPacket> {
    public static final AcceptTeleportationSerializer_v754 INSTANCE = new AcceptTeleportationSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, AcceptTeleportationPacket packet) throws PacketSerializeException {
        VarInts.writeUnsignedInt(buffer, packet.getId());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, AcceptTeleportationPacket packet) throws PacketSerializeException {
        packet.setId(VarInts.readUnsignedInt(buffer));
    }
}
