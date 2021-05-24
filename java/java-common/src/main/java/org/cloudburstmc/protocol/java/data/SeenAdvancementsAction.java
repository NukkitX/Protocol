package org.cloudburstmc.protocol.java.data;

public enum SeenAdvancementsAction {
    OPENED_TAB,
    CLOSED_SCREEN;

    public static final SeenAdvancementsAction[] VALUES = values();

    public static SeenAdvancementsAction getById(int id) {
        return VALUES.length > id ? VALUES[id] : null;
    }
}
