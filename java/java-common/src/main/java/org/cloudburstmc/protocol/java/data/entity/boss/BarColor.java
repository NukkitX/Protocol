package org.cloudburstmc.protocol.java.data.entity.boss;

public enum BarColor {
    PINK,
    BLUE,
    RED,
    GREEN,
    YELLOW,
    PURPLE,
    WHITE;

    private static final BarColor[] VALUES = values();

    public static BarColor getById(int id) {
        return VALUES[id];
    }
}
