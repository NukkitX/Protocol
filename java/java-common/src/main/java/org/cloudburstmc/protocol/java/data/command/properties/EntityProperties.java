package org.cloudburstmc.protocol.java.data.command.properties;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class EntityProperties implements CommandProperties {
    boolean singleTarget;
    boolean playersOnly;
}
