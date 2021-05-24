package org.cloudburstmc.protocol.java.data.command;

public enum CommandType {
    ROOT,
    LITERAL,
    ARGUMENT;

    private static final CommandType[] VALUES = values();

    public static CommandType getById(int id) {
        return VALUES[id];
    }
}
