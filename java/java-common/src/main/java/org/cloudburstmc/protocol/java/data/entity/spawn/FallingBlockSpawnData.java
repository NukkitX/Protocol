package org.cloudburstmc.protocol.java.data.entity.spawn;

public class FallingBlockSpawnData extends SpawnData {
    public FallingBlockSpawnData(int id, int data) {
        this(id | (data << 12));
    }

    public FallingBlockSpawnData(int value) {
        super(value);
    }

    public int getId() {
        return this.value & 65535;
    }

    public int getData() {
        return this.value >> 12;
    }
}
