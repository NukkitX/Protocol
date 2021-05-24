package org.cloudburstmc.protocol.java.data.command;

public enum CommandParser {
    ANGLE,
    BOOL,
    DOUBLE,
    FLOAT,
    INTEGER,
    STRING,
    ENTITY,
    GAME_PROFILE,
    BLOCK_POS,
    COLUMN_POS,
    VEC3,
    VEC2,
    BLOCK_STATE,
    BLOCK_PREDICATE,
    ITEM_STACK,
    ITEM_PREDICATE,
    COLOR,
    COMPONENT,
    MESSAGE,
    NBT,
    NBT_COMPOUND_TAG,
    NBT_TAG,
    NBT_PATH,
    OBJECTIVE,
    OBJECTIVE_CRITERIA,
    OPERATION,
    PARTICLE,
    ROTATION,
    SCOREBOARD_SLOT,
    SCORE_HOLDER,
    SWIZZLE,
    UUID,
    TEAM,
    ITEM_SLOT,
    RESOURCE_LOCATION,
    MOB_EFFECT,
    FUNCTION,
    ENTITY_ANCHOR,
    RANGE,
    INT_RANGE,
    FLOAT_RANGE,
    ITEM_ENCHANTMENT,
    ENTITY_SUMMON,
    DIMENSION,
    TIME;

    private static final CommandParser[] VALUES = values();

    public static CommandParser getById(int id) {
        return VALUES[id];
    }
}
