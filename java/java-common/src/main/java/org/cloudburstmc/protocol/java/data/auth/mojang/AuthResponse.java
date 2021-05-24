package org.cloudburstmc.protocol.java.data.auth.mojang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private final String accessToken;
    private final String clientToken;
    private final GameProfile selectedProfile;
    private final GameProfile[] availableProfiles;
}
