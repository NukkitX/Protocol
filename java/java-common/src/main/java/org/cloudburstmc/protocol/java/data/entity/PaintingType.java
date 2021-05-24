package org.cloudburstmc.protocol.java.data.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaintingType {
    KEBAB(0, 0, 0, 16, 16),
    AZTEC(1, 16, 0, 16, 16),
    ALBAN(2, 32, 0, 16, 16),
    AZTEC2(3, 48, 0, 16, 16),
    BOMB(4, 64, 0, 16, 16),
    PLANT(5, 80, 0, 16, 16),
    WASTELAND(6, 96, 0, 16, 16),
    POOL(7, 0, 32, 32, 16),
    COURBET(8, 32, 32, 32, 16),
    SEA(9, 64, 32, 32, 16),
    SUNSET(10, 96, 32, 32, 16),
    CREEBET(11, 128, 32, 32, 16),
    WANDERER(12, 0, 64, 16, 32),
    GRAHAM(13, 16, 64, 16, 32),
    MATCH(14, 0, 128, 32, 32),
    BUEST(15, 32, 128, 32, 32),
    STAGE(16, 64, 128, 32, 32),
    VOID(17, 96, 128, 32, 32),
    SKULL_AND_ROSES(18, 128, 128, 32, 32),
    WITHER(19, 160, 128, 32, 32),
    FIGHTERS(20, 0, 96, 64, 32),
    POINTER(21, 0, 192, 64, 64),
    PIGSCENE(22, 64, 192, 64, 64),
    BURNING_SKULL(23, 128, 192, 64, 64),
    SKELETON(24, 192, 64, 64, 48),
    DONKEY_KONG(25, 192, 112, 64, 48);

    private static final PaintingType[] VALUES = values();

    private final int id;
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public static PaintingType getById(int id) {
        return VALUES[id];
    }
}
