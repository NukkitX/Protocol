package org.cloudburstmc.protocol.java.data.entity.boss;

public enum BarDivision {
    NONE,
    NOTCHED_6,
    NOTCHED_10,
    NOTCHED_12,
    NOTCHED_20;

    private static final BarDivision[] VALUES = values();

    public static BarDivision getById(int id) {
        return VALUES[id];
    }
}
