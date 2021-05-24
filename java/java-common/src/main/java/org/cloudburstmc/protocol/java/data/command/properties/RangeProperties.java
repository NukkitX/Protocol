package org.cloudburstmc.protocol.java.data.command.properties;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class RangeProperties implements CommandProperties {
    boolean allowDecimals;
}
