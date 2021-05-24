package org.cloudburstmc.protocol.java.data.auth.mojang;

import lombok.Value;
import org.cloudburstmc.protocol.java.data.profile.property.PropertyMap;

import java.util.UUID;

@Value
public class HasJoinedResponse {
    UUID id;
    PropertyMap properties;
}
