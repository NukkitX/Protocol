package org.cloudburstmc.protocol.java.data.command.properties;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FloatProperties implements CommandProperties {
    float min;
    float max;

    public FloatProperties() {
        this.min = -Float.MAX_VALUE;
        this.max = Float.MAX_VALUE;
    }
}
