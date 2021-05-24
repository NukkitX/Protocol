package org.cloudburstmc.protocol.java.packet.type;

import org.cloudburstmc.protocol.java.packet.State;

public interface JavaPacketType {
    Direction getDirection();

    State getState();

    enum Direction {
        CLIENTBOUND,
        SERVERBOUND,
        BI_DIRECTIONAL
    }
}
