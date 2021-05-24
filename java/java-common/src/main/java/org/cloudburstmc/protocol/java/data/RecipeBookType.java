package org.cloudburstmc.protocol.java.data;

public enum RecipeBookType {
    CRAFTING,
    FURNACE,
    BLASTING_FURNACE,
    SMOKER;

    public static final RecipeBookType[] VALUES = values();

    public static RecipeBookType getById(int id) {
        return VALUES.length > id ? VALUES[id] : null;
    }
}
