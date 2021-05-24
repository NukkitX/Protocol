package org.cloudburstmc.protocol.java.data;

public enum Direction {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    EAST,
    WEST;

    private static final Direction[] VALUES = values();

    public static Direction getById(int id) {
        return VALUES[id];
    }
}
