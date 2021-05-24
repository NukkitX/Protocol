package org.cloudburstmc.protocol.java.handler;

import org.cloudburstmc.protocol.java.packet.status.*;

public interface JavaStatusPacketHandler extends JavaPacketHandler {

    default boolean handle(StatusResponsePacket packet) {
        return false;
    }

    default boolean handle(PongPacket packet) {
        return false;
    }

    default boolean handle(StatusRequestPacket packet) {
        return false;
    }

    default boolean handle(PingPacket packet) {
        return false;
    }
}
