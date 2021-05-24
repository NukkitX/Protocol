package org.cloudburstmc.protocol.java.data.entity.boss;

public enum BarAction {
    ADD,
    REMOVE,
    UPDATE_HEALTH,
    UPDATE_TITLE,
    UPDATE_STYLE,
    UPDATE_FLAGS;

    private static final BarAction[] VALUES = values();

    public static BarAction getById(int id) {
        return VALUES[id];
    }
}
