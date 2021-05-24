package org.cloudburstmc.protocol.java.data.entity;

public enum Animation {
    SWING_MAIN_HAND,
    TAKE_DAMAGE,
    LEAVE_BED,
    SWING_OFFHAND,
    CRITICAL_EFFECT,
    MAGIC_CRITICAL_EFFECT;

    private static final Animation[] VALUES = values();

    public static Animation getById(int id) {
        return VALUES[id];
    }
}
