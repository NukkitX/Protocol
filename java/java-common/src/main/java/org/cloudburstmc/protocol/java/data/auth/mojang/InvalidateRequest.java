package org.cloudburstmc.protocol.java.data.auth.mojang;

import lombok.Value;

@Value
public class InvalidateRequest {
    String clientToken;
    String accessToken;
}
