package org.cloudburstmc.protocol.java.data.auth.mojang;

import lombok.Value;

import java.util.UUID;

@Value
public class JoinServerRequest {
    String accessToken;
    UUID selectedProfile;
    String serverId;
}
