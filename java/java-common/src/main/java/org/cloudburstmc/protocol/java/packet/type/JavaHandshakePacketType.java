package org.cloudburstmc.protocol.java.packet.type;

import lombok.AllArgsConstructor;
import org.cloudburstmc.protocol.java.packet.State;

@AllArgsConstructor
public enum JavaHandshakePacketType implements JavaPacketType {
    HANDSHAKE(Direction.SERVERBOUND),
    LEGACY_SERVER_LIST_PING(Direction.SERVERBOUND);

    private final Direction direction;

    @Override
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public State getState() {
        return State.HANDSHAKING;
    }
}
