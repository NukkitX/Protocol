package org.cloudburstmc.protocol.java.data.command;

public enum CommandAction {
    PERFORM_RESPAWN,
    REQUEST_STATS;

    public static final CommandAction[] VALUES = values();

    public static CommandAction getById(int id) {
        return VALUES.length > id ? VALUES[id] : null;
    }
}
