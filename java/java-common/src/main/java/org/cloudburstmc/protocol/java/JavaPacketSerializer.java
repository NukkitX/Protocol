package org.cloudburstmc.protocol.java;

import com.nukkitx.protocol.exception.PacketSerializeException;
import com.nukkitx.protocol.serializer.PacketSerializer;
import io.netty.buffer.ByteBuf;

public interface JavaPacketSerializer<T extends JavaPacket<?>> extends PacketSerializer<T, JavaPacketHelper> {

    @Override
    default void serialize(ByteBuf buffer, JavaPacketHelper helper, T packet) throws PacketSerializeException {
    }

    default void serialize(ByteBuf buffer, JavaPacketHelper helper, T packet, JavaSession session) throws PacketSerializeException {
        this.serialize(buffer, helper, packet);
    }

    @Override
    default void deserialize(ByteBuf buffer, JavaPacketHelper helper, T packet) throws PacketSerializeException {
    }

    default void deserialize(ByteBuf buffer, JavaPacketHelper helper, T packet, JavaSession session) throws PacketSerializeException {
        this.deserialize(buffer, helper, packet);
    }
}
