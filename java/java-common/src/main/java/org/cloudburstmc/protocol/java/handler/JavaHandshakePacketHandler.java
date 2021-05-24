package org.cloudburstmc.protocol.java.handler;

import org.cloudburstmc.protocol.java.packet.handshake.HandshakingPacket;
import org.cloudburstmc.protocol.java.packet.handshake.LegacyServerListPingPacket;

public interface JavaHandshakePacketHandler extends JavaPacketHandler {

    default boolean handle(HandshakingPacket packet) {
        return false;
    }

    default boolean handle(LegacyServerListPingPacket packet) {
        return false;
    }
}
