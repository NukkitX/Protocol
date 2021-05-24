package org.cloudburstmc.protocol.java.v754.serializer.handshake;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.State;
import org.cloudburstmc.protocol.java.packet.handshake.HandshakingPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HandshakingSerializer_v754 implements JavaPacketSerializer<HandshakingPacket> {
    public static final HandshakingSerializer_v754 INSTANCE = new HandshakingSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, HandshakingPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getProtocolVersion());
        helper.writeString(buffer, packet.getAddress());
        buffer.writeShort(packet.getPort());
        VarInts.writeUnsignedInt(buffer, packet.getNextState().getId());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, HandshakingPacket packet) {
        packet.setProtocolVersion(VarInts.readUnsignedInt(buffer));
        packet.setAddress(helper.readString(buffer));
        packet.setPort(buffer.readUnsignedShort());
        packet.setNextState(State.getById(VarInts.readUnsignedInt(buffer)));
    }
}
