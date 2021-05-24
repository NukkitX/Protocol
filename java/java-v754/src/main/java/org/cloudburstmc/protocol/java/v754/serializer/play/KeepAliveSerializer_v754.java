package org.cloudburstmc.protocol.java.v754.serializer.play;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.KeepAlivePacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeepAliveSerializer_v754 implements JavaPacketSerializer<KeepAlivePacket> {
    public static final KeepAliveSerializer_v754 INSTANCE = new KeepAliveSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, KeepAlivePacket packet) throws PacketSerializeException {
        buffer.writeLong(packet.getId());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, KeepAlivePacket packet) throws PacketSerializeException {
        packet.setId(buffer.readLong());
    }
}
