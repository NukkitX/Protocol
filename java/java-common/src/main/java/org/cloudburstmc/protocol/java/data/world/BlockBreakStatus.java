package org.cloudburstmc.protocol.java.data.world;

public enum BlockBreakStatus {
    START_DIGGING,
    CANCEL_DIGGING,
    FINISH_DIGGING;

    private static final BlockBreakStatus[] VALUES = values();

    public static BlockBreakStatus getById(int id) {
        return VALUES[id];
    }
}
