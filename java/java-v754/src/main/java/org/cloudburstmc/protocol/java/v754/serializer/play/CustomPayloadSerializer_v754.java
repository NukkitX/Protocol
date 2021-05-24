package org.cloudburstmc.protocol.java.v754.serializer.play;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.CustomPayloadPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomPayloadSerializer_v754 implements JavaPacketSerializer<CustomPayloadPacket> {
    public static final CustomPayloadSerializer_v754 INSTANCE = new CustomPayloadSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, CustomPayloadPacket packet) throws PacketSerializeException {
        helper.writeKey(buffer, packet.getChannel());
        buffer.writeBytes(packet.getBuffer());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, CustomPayloadPacket packet) throws PacketSerializeException {
        packet.setChannel(helper.readKey(buffer));
        packet.setBuffer(buffer.readBytes(buffer.readableBytes()).retain());
    }
}
