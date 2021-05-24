package org.cloudburstmc.protocol.java.data.entity.spawn;

import org.cloudburstmc.protocol.java.data.Direction;

public class DirectionSpawnData extends SpawnData {
    public DirectionSpawnData(Direction direction) {
        this(direction.ordinal());
    }

    public DirectionSpawnData(int value) {
        super(value);
    }

    public Direction getDirection() {
        return Direction.getById(this.value);
    }
}
