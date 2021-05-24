package org.cloudburstmc.protocol.java.data.text;

public enum ChatVisibility {
    FULL, SYSTEM, HIDDEN;

    public static final ChatVisibility[] VALUES = values();

    public static ChatVisibility getById(int id) {
        return id < VALUES.length ? VALUES[id] : null;
    }
}
