package org.cloudburstmc.protocol.java.data.command.properties;

public enum StringProperties implements CommandProperties {
    SINGLE_WORD,
    QUOTABLE_PHRASE,
    GREEDY_PHRASE;

    private static final StringProperties[] VALUES = values();

    public static StringProperties getById(int id) {
        return VALUES[id];
    }
}
