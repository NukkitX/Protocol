package org.cloudburstmc.protocol.java.v754.serializer.handshake;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.handshake.LegacyServerListPingPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LegacyServerListPingSerializer_v754 implements JavaPacketSerializer<LegacyServerListPingPacket> {
    public static final LegacyServerListPingSerializer_v754 INSTANCE = new LegacyServerListPingSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, LegacyServerListPingPacket packet) {
        buffer.writeByte(packet.getPayload());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, LegacyServerListPingPacket packet) {
        packet.setPayload((byte) buffer.readUnsignedByte());
    }
}