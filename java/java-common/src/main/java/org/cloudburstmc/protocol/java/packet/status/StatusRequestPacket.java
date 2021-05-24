package org.cloudburstmc.protocol.java.packet.status;

import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.handler.JavaStatusPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaStatusPacketType;

// Empty packet
public class StatusRequestPacket extends JavaPacket<JavaStatusPacketHandler> {

    @Override
    public boolean handle(JavaStatusPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaStatusPacketType.STATUS_REQUEST;
    }
}
