package org.cloudburstmc.protocol.java.data;

public enum SoundSource {
    MASTER,
    MUSIC,
    RECORDS,
    WEATHER,
    BLOCKS,
    HOSTILE,
    NEUTRAL,
    PLAYERS,
    AMBIENT,
    VOICE;

    private static final SoundSource[] VALUES = values();

    public static SoundSource getById(int id) {
        return VALUES.length > id ? VALUES[id] : null;
    }
}
