package org.cloudburstmc.protocol.java.data.command.properties;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class IntegerProperties implements CommandProperties {
    int min;
    int max;

    public IntegerProperties() {
        this.min = Integer.MIN_VALUE;
        this.max = Integer.MAX_VALUE;
    }
}
