package org.cloudburstmc.protocol.java.data;

public enum PlayerAction {
    START_DESTROY_BLOCK,
    ABORT_DESTROY_BLOCK,
    STOP_DESTROY_BLOCK,
    DROP_ALL_ITEMS,
    DROP_ITEM,
    RELEASE_USE_ITEM,
    SWAP_ITEM_WITH_OFFHAND;

    public static final PlayerAction[] VALUES = values();

    public static PlayerAction getById(int id) {
        return VALUES.length > id ? VALUES[id] : null;
    }
}
