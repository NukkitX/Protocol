package org.cloudburstmc.protocol.java.data;

import lombok.Value;
import net.kyori.adventure.text.Component;

@Value
public class MapDecoration {
    MapDecorationType type;
    byte x;
    byte y;
    byte rotation;
    Component name;
}
