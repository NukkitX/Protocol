package org.cloudburstmc.protocol.java.data;

public enum MapDecorationType {
    PLAYER,
    FRAME,
    RED_MARKER,
    BLUE_MARKER,
    TARGET_X,
    TARGET_POINT,
    PLAYER_OFF_MAP,
    PLAYER_OFF_LIMITS,
    MANSION,
    MONUMENT,
    BANNER_WHITE,
    BANNER_ORANGE,
    BANNER_MAGENTA,
    BANNER_LIGHT_BLUE,
    BANNER_YELLOW,
    BANNER_LIME,
    BANNER_PINK,
    BANNER_GRAY,
    BANNER_LIGHT_GRAY,
    BANNER_CYAN,
    BANNER_PURPLE,
    BANNER_BLUE,
    BANNER_BROWN,
    BANNER_GREEN,
    BANNER_RED,
    BANNER_BLACK,
    RED_X;

    private static final MapDecorationType[] VALUES = values();

    public static MapDecorationType getById(int id) {
        return VALUES.length > id ? VALUES[id] : null;
    }
}
