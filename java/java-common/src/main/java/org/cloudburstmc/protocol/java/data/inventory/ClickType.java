package org.cloudburstmc.protocol.java.data.inventory;

public enum ClickType {
    PICKUP,
    QUICK_MOVE,
    SWAP,
    CLONE,
    THROW,
    QUICK_CRAFT,
    PICKUP_ALL;

    public static final ClickType[] VALUES = values();

    public static ClickType getById(int id) {
        return VALUES.length > id ? VALUES[id] : null;
    }
}
