package org.cloudburstmc.protocol.java.packet.type;

import lombok.AllArgsConstructor;
import org.cloudburstmc.protocol.java.packet.State;

@AllArgsConstructor
public enum JavaStatusPacketType implements JavaPacketType {
    STATUS_RESPONSE(Direction.CLIENTBOUND),
    PONG(Direction.CLIENTBOUND),

    STATUS_REQUEST(Direction.SERVERBOUND),
    PING(Direction.SERVERBOUND);

    private final Direction direction;

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public State getState() {
        return State.STATUS;
    }
}
