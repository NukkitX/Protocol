package org.cloudburstmc.protocol.java.data.world;

public enum Difficulty {
    PEACEFUL,
    EASY,
    NORMAL,
    HARD;

    private static final Difficulty[] VALUES = values();

    public static Difficulty getById(int id) {
        return VALUES[id];
    }
}
