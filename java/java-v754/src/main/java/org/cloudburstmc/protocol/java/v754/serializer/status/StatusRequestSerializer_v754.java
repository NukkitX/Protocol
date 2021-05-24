package org.cloudburstmc.protocol.java.v754.serializer.status;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.status.StatusRequestPacket;

// Empty packet
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatusRequestSerializer_v754 implements JavaPacketSerializer<StatusRequestPacket> {
    public static final StatusRequestSerializer_v754 INSTANCE = new StatusRequestSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, StatusRequestPacket packet) {
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, StatusRequestPacket packet) {
    }
}
