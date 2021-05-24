package org.cloudburstmc.protocol.java.data.command.properties;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class DoubleProperties implements CommandProperties {
    double min;
    double max;

    public DoubleProperties() {
        this.min = -Double.MAX_VALUE;
        this.max = Double.MAX_VALUE;
    }
}
