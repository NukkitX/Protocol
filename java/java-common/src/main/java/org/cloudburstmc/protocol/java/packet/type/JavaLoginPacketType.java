package org.cloudburstmc.protocol.java.packet.type;

import lombok.AllArgsConstructor;
import org.cloudburstmc.protocol.java.packet.State;

@AllArgsConstructor
public enum JavaLoginPacketType implements JavaPacketType {
    DISCONNECT(Direction.CLIENTBOUND),
    ENCRYPTION_REQUEST(Direction.CLIENTBOUND),
    LOGIN_SUCCESS(Direction.CLIENTBOUND),
    SET_COMPRESSION(Direction.CLIENTBOUND),
    CUSTOM_QUERY_REQUEST(Direction.CLIENTBOUND),

    LOGIN_START(Direction.SERVERBOUND),
    ENCRYPTION_RESPONSE(Direction.SERVERBOUND),
    CUSTOM_QUERY_RESPONSE(Direction.SERVERBOUND);

    private final Direction direction;

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public State getState() {
        return State.LOGIN;
    }
}
