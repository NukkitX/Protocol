package org.cloudburstmc.protocol.java.data.statistic;

public enum StatisticCategory {
    MINED,
    CRAFTED,
    USED,
    BROKEN,
    PICKED_UP,
    DROPPED,
    KILLED,
    KILLED_BY,
    CUSTOM;

    private static final StatisticCategory[] VALUES = values();

    public static StatisticCategory getById(int id) {
        return VALUES[id];
    }
}
