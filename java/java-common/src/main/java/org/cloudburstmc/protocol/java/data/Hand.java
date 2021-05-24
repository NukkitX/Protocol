package org.cloudburstmc.protocol.java.data;

public enum Hand {
    MAIN_HAND,
    OFF_HAND;

    public static final Hand[] VALUES = values();

    public static Hand getById(int id) {
        return id < VALUES.length ? VALUES[id] : null;
    }
}
