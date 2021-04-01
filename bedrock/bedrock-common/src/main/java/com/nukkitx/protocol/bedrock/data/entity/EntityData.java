package com.nukkitx.protocol.bedrock.data.entity;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@Getter
@RequiredArgsConstructor
public enum EntityData {
    FLAGS(Type.FLAGS, true),
    HEALTH(Type.INT),
    VARIANT(Type.INT),
    COLOR(Type.BYTE),
    NAMETAG(Type.STRING),
    OWNER_EID(Type.LONG),
    TARGET_EID(Type.LONG),
    AIR_SUPPLY(Type.SHORT),
    EFFECT_COLOR(Type.INT),
    EFFECT_AMBIENT(Type.BYTE),
    JUMP_DURATION(Type.BYTE),
    HURT_TIME(Type.INT),
    HURT_DIRECTION(Type.INT),
    ROW_TIME_LEFT(Type.FLOAT),
    ROW_TIME_RIGHT(Type.FLOAT),
    EXPERIENCE_VALUE(Type.INT),
    DISPLAY_ITEM(Type.INT),
    DISPLAY_OFFSET(null), // Can be long or int
    CUSTOM_DISPLAY(Type.BYTE),
    // These values are stored server side
    @Deprecated
    SWELL(Type.INT),
    @Deprecated
    OLD_SWELL(Type.INT),
    @Deprecated
    SWELL_DIRECTION(Type.INT),
    CHARGE_AMOUNT(Type.BYTE),
    CARRIED_BLOCK(Type.INT),
    CLIENT_EVENT(Type.BYTE),
    USING_ITEM(Type.BYTE),
    PLAYER_FLAGS(Type.BYTE),
    PLAYER_INDEX(Type.INT),
    BED_POSITION(Type.VECTOR3I),
    X_POWER(Type.FLOAT),
    Y_POWER(Type.FLOAT),
    Z_POWER(Type.FLOAT),
    AUX_POWER(null),
    FISH_X(null),
    FISH_Z(null),
    FISH_ANGLE(null),
    POTION_AUX_VALUE(Type.SHORT),
    LEASH_HOLDER_EID(Type.LONG),
    SCALE(Type.FLOAT),
    HAS_NPC_COMPONENT(Type.BYTE),
    SKIN_ID(Type.STRING),
    NPC_SKIN_ID(Type.STRING),
    NPC_DATA(null),
    URL_TAG(Type.STRING),
    MAX_AIR_SUPPLY(Type.SHORT),
    MARK_VARIANT(Type.INT),
    CONTAINER_TYPE(Type.BYTE),
    CONTAINER_BASE_SIZE(Type.INT),
    CONTAINER_STRENGTH_MODIFIER(Type.INT),
    BLOCK_TARGET(Type.VECTOR3I),
    WITHER_INVULNERABLE_TICKS(Type.INT),
    WITHER_TARGET_1(Type.LONG),
    WITHER_TARGET_2(Type.LONG),
    WITHER_TARGET_3(Type.LONG),
    WITHER_AERIAL_ATTACK(Type.SHORT),
    BOUNDING_BOX_WIDTH(Type.FLOAT),
    BOUNDING_BOX_HEIGHT(Type.FLOAT),
    FUSE_LENGTH(Type.INT),
    RIDER_SEAT_POSITION(Type.VECTOR3F),
    RIDER_ROTATION_LOCKED(Type.BYTE),
    RIDER_MAX_ROTATION(Type.FLOAT),
    RIDER_MIN_ROTATION(Type.FLOAT),
    RIDER_ROTATION_OFFSET(null),
    AREA_EFFECT_CLOUD_RADIUS(Type.FLOAT),
    AREA_EFFECT_CLOUD_WAITING(Type.INT),
    AREA_EFFECT_CLOUD_PARTICLE_ID(Type.INT),
    SHULKER_PEAK_HEIGHT(Type.INT),
    SHULKER_PEEK_ID(null),
    SHULKER_ATTACH_FACE(Type.BYTE),
    SHULKER_ATTACH_POS(Type.VECTOR3I),
    TRADE_TARGET_EID(Type.LONG),
    COMMAND_BLOCK_ENABLED(Type.BYTE),
    COMMAND_BLOCK_COMMAND(Type.STRING),
    COMMAND_BLOCK_LAST_OUTPUT(Type.STRING),
    COMMAND_BLOCK_TRACK_OUTPUT(Type.BYTE),
    CONTROLLING_RIDER_SEAT_INDEX(Type.BYTE),
    STRENGTH(Type.INT),
    MAX_STRENGTH(Type.INT),
    EVOKER_SPELL_COLOR(Type.INT),
    LIMITED_LIFE(Type.INT),
    ARMOR_STAND_POSE_INDEX(Type.INT),
    ENDER_CRYSTAL_TIME_OFFSET(Type.INT),
    NAMETAG_ALWAYS_SHOW(Type.BYTE),
    COLOR_2(Type.BYTE),
    SCORE_TAG(Type.STRING),
    BALLOON_ATTACHED_ENTITY(Type.LONG),
    PUFFERFISH_SIZE(Type.BYTE),
    BOAT_BUBBLE_TIME(Type.INT),
    AGENT_ID(Type.LONG),
    SITTING_AMOUNT(null),
    SITTING_AMOUNT_PREVIOUS(null),
    EATING_COUNTER(Type.INT),
    FLAGS_2(Type.FLAGS, true),
    LAYING_AMOUNT(null),
    LAYING_AMOUNT_PREVIOUS(null),
    AREA_EFFECT_CLOUD_DURATION(Type.INT),
    AREA_EFFECT_CLOUD_SPAWN_TIME(Type.INT),
    AREA_EFFECT_CLOUD_CHANGE_RATE(Type.FLOAT),
    AREA_EFFECT_CLOUD_CHANGE_ON_PICKUP(Type.FLOAT),
    AREA_EFFECT_CLOUD_COUNT(Type.INT),
    INTERACTIVE_TAG(Type.STRING),
    TRADE_TIER(Type.INT),
    MAX_TRADE_TIER(Type.INT),
    TRADE_XP(Type.INT),
    SPAWNING_FRAMES(null),
    COMMAND_BLOCK_TICK_DELAY(Type.INT),
    COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK(Type.BYTE),
    AMBIENT_SOUND_INTERVAL(Type.FLOAT),
    AMBIENT_SOUND_INTERVAL_RANGE(Type.FLOAT),
    AMBIENT_SOUND_EVENT_NAME(Type.STRING),
    FALL_DAMAGE_MULTIPLIER(Type.FLOAT),
    NAME_RAW_TEXT(null),
    CAN_RIDE_TARGET(Type.BYTE),
    LOW_TIER_CURED_TRADE_DISCOUNT(Type.INT),
    HIGH_TIER_CURED_TRADE_DISCOUNT(Type.INT),
    NEARBY_CURED_TRADE_DISCOUNT(Type.INT),
    NEARBY_CURED_DISCOUNT_TIME_STAMP(Type.INT),
    HITBOX(Type.NBT),
    IS_BUOYANT(Type.BYTE),
    BUOYANCY_DATA(Type.STRING),
    GENOA_ENTITY_DATA(Type.BYTE), // TODO: Same as GenoaPacketHelper, find out what this is
    /**
     * @since v428
     */
    FREEZING_EFFECT_STRENGTH(null),
    /**
     * @since v428
     */
    GOAT_HORN_COUNT(null);

    private final Type type;
    private final boolean flags;

    EntityData(Type type) {
        this.type = type;
        this.flags = false;
    }

    @RequiredArgsConstructor
    public enum Type {
        FLAGS,
        BYTE,
        SHORT,
        INT,
        FLOAT,
        STRING,
        NBT,
        VECTOR3I,
        LONG,
        VECTOR3F;

        @Nonnull
        public static Type from(Object o) {
            if (o instanceof EntityFlags) {
                return EntityData.Type.FLAGS;
            } else if (o instanceof Byte) {
                return EntityData.Type.BYTE;
            } else if (o instanceof Short) {
                return EntityData.Type.SHORT;
            } else if (o instanceof Integer) {
                return EntityData.Type.INT;
            } else if (o instanceof Float) {
                return EntityData.Type.FLOAT;
            } else if (o instanceof String) {
                return EntityData.Type.STRING;
            } else if (o instanceof ItemData || o instanceof NbtMap) {
                return EntityData.Type.NBT;
            } else if (o instanceof Vector3i) {
                return EntityData.Type.VECTOR3I;
            } else if (o instanceof Long) {
                return EntityData.Type.LONG;
            } else if (o instanceof Vector3f) {
                return EntityData.Type.VECTOR3F;
            }
            throw new IllegalArgumentException("Invalid type");
        }
    }
}
