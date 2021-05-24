package org.cloudburstmc.protocol.java.data.text;

public enum ChatPosition {
    CHAT_BOX,
    SYSTEM,
    GAME_INFO;

    private static final ChatPosition[] VALUES = values();

    public static ChatPosition getById(int id) {
        return VALUES[id];
    }
}
