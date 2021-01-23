package org.cloudburstmc.protocol.java.data.statistic;

public enum StatisticType {
    LEAVE_GAME,
    PLAY_ONE_MINUTE,
    TIME_SINCE_DEATH,
    SNEAK_TIME,
    WALK_ONE_CM,
    CROUCH_ONE_CM,
    SPRINT_ONE_CM,
    SWIM_ONE_CM,
    FALL_ONE_CM,
    CLIMB_ONE_CM,
    FLY_ONE_CM,
    DIVE_ONE_CM,
    MINECART_ONE_CM,
    BOAT_ONE_CM,
    PIG_ONE_CM,
    HORSE_ONE_CM,
    AVIATE_ONE_CM,
    JUMP,
    DROP,
    DAMAGE_DEALT,
    DAMAGE_TAKEN,
    DEATHS,
    MOB_KILLS,
    ANIMALS_BRED,
    PLAYER_KILLS,
    FISH_CAUGHT,
    TALKED_TO_VILLAGER,
    TRADED_WITH_VILLAGER,
    EAT_CAKE_SLICE,
    FILL_CAULDRON,
    USE_CAULDRON,
    CLEAN_ARMOR,
    CLEAN_BANNER,
    INTERACT_WITH_BREWINGSTAND,
    INTERACT_WITH_BEACON,
    INSPECT_DROPPER,
    INSPECT_HOPPER,
    INSPECT_DISPENSER,
    PLAY_NOTEBLOCK,
    TUNE_NOTEBLOCK,
    POT_FLOWER,
    TRIGGER_TRAPPED_CHEST,
    OPEN_ENDERCHEST,
    ENCHANT_ITEM,
    PLAY_RECORD,
    INTERACT_WITH_FURNACE,
    INTERACT_WITH_CRAFTING_TABLE,
    OPEN_CHEST,
    SLEEP_IN_BED,
    OPEN_SHULKER_BOX;

    private static final StatisticType[] VALUES = values();

    public static StatisticType getById(int id) {
        return VALUES[id];
    }
}