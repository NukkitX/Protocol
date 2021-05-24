package org.cloudburstmc.protocol.java.data.auth.mojang;

import lombok.Getter;
import lombok.Value;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;
import org.cloudburstmc.protocol.java.data.profile.property.PropertyMap;

@Getter
public class AuthRefreshResponse extends AuthResponse {
    private final User user;

    public AuthRefreshResponse(String accessToken, String clientToken, GameProfile selectedProfile, GameProfile[] availableProfiles, User user) {
        super(accessToken, clientToken, selectedProfile, availableProfiles);

        this.user = user;
    }

    @Value
    public static class User {
        String id;
        PropertyMap properties;
    }
}
