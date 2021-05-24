package org.cloudburstmc.protocol.java.data.profile.property;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class Property {
    String name;
    String value;
    String signature;

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
        this.signature = null;
    }
}
