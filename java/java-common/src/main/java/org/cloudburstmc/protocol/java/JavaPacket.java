package org.cloudburstmc.protocol.java;

import com.nukkitx.protocol.MinecraftPacket;
import com.nukkitx.protocol.PacketHandler;
import lombok.Data;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;

@Data
public abstract class JavaPacket<T extends PacketHandler> implements MinecraftPacket {

    public abstract boolean handle(T handler);

    public abstract JavaPacketType getPacketType();
}
