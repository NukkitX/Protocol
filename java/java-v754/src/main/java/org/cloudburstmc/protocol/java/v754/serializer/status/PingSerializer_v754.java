package org.cloudburstmc.protocol.java.v754.serializer.status;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.status.PingPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PingSerializer_v754 implements JavaPacketSerializer<PingPacket> {
    public static final PingSerializer_v754 INSTANCE = new PingSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, PingPacket packet) {
        buffer.writeLong(packet.getTimestamp());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, PingPacket packet) {
        packet.setTimestamp(buffer.readLong());
    }
}
