package org.cloudburstmc.protocol.java.data;

public enum GameType {
    SURVIVAL,
    CREATIVE,
    ADVENTURE,
    SPECTATOR;

    private static final GameType[] VALUES = values();

    public static GameType getById(int id) {
        return VALUES[id];
    }
}
