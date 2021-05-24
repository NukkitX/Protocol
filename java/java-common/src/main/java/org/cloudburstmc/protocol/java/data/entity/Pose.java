package org.cloudburstmc.protocol.java.data.entity;

public enum Pose {
    STANDING,
    FALL_FLYING,
    SLEEPING,
    SWIMMING,
    SPIN_ATTACK,
    CROUCHING,
    DYING;

    private static final Pose[] VALUES = values();

    public static Pose getById(int id) {
        return VALUES.length > id ? VALUES[id] : null;
    }
}
