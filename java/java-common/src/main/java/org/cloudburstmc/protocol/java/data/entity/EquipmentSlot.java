package org.cloudburstmc.protocol.java.data.entity;

public enum EquipmentSlot {
    MAIN_HAND,
    OFF_HAND,
    HELMET,
    CHESTPLATE,
    LEGGINGS,
    BOOTS;

    private static final EquipmentSlot[] VALUES = values();

    public static EquipmentSlot getById(int id) {
        return id < VALUES.length ? VALUES[id] : null;
    }
}
