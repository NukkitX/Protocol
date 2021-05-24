package org.cloudburstmc.protocol.java.data.entity;

public enum MinecartType {
    EMPTY,
    CHEST,
    POWERED,
    TNT,
    MOB_SPAWNER,
    HOPPER,
    COMMAND_BLOCK;

    private static final MinecartType[] VALUES = values();

    public static MinecartType getById(int id) {
        return VALUES[id];
    }
}
