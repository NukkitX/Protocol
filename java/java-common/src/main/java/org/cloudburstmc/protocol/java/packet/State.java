package org.cloudburstmc.protocol.java.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum State {
    HANDSHAKING,
    PLAY,
    STATUS,
    LOGIN;

    private static final State[] VALUES = values();

    public static State getById(int id) {
        return VALUES[id + 1];
    }

    public int getId() {
        return this.ordinal() - 1;
    }
}
