package org.cloudburstmc.protocol.java.data.entity.spawn;

import org.cloudburstmc.protocol.java.data.entity.MinecartType;

public class MinecartSpawnData extends SpawnData {
    public MinecartSpawnData(MinecartType type) {
        this(type.ordinal());
    }

    public MinecartSpawnData(int value) {
        super(value);
    }

    public MinecartType getMinecartType() {
        return MinecartType.getById(this.value);
    }
}
