package org.cloudburstmc.protocol.java.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PositionFlag {
    X(0x01),
    Y(0x02),
    Z(0x04),
    Y_ROT(0x08),
    X_ROT(0x10);

    public static final PositionFlag[] VALUES = values();

    private final int bitMask;
}
