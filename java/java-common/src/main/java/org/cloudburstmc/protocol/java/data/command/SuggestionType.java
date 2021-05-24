package org.cloudburstmc.protocol.java.data.command;

public enum SuggestionType {
    ASK_SERVER,
    ALL_RECIPES,
    AVAILABLE_SOUNDS,
    AVAILABLE_BIOMES,
    SUMMONABLE_ENTITIES;

    private static final SuggestionType[] VALUES = values();

    public static SuggestionType getById(int id) {
        return VALUES[id];
    }
}
