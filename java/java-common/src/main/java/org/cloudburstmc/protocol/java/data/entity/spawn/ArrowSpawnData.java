package org.cloudburstmc.protocol.java.data.entity.spawn;

public class ArrowSpawnData extends SpawnData {
    public ArrowSpawnData(int value) {
        super(value);
    }

    public int getArrowId() {
        return this.value - 1;
    }
}
