package org.cloudburstmc.protocol.java.data.auth.mojang;

import lombok.Value;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;

@Value
public class RefreshRequest {
    String clientToken;
    String accessToken;
    GameProfile selectedProfile;
    boolean requestUser;
}
