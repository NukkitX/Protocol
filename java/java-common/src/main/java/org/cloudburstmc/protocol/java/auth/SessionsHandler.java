package org.cloudburstmc.protocol.java.auth;

import com.google.gson.JsonElement;
import lombok.experimental.UtilityClass;
import org.cloudburstmc.protocol.java.data.auth.mojang.HasJoinedResponse;
import org.cloudburstmc.protocol.java.data.auth.mojang.JoinServerRequest;
import org.cloudburstmc.protocol.java.data.auth.mojang.ProfilePropertiesResponse;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;
import org.cloudburstmc.protocol.java.util.HttpUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

import static org.cloudburstmc.protocol.java.Java.GSON;

@UtilityClass
public class SessionsHandler {
    private static final URI SESSIONS_URI = URI.create("https://sessionserver.mojang.com/session/minecraft/");
    private static final String HAS_JOINED_ENDPOINT = "hasJoined";
    private static final String JOIN_ENDPOINT = "join";
    private static final String PROFILE_ENDPOINT = "profile";

    public CompletableFuture<JsonElement> joinServer(String accessToken, GameProfile profile, String serverId) {
        JoinServerRequest request = new JoinServerRequest(accessToken, profile.getId(), serverId);
        return HttpUtils.createPostRequest(SESSIONS_URI.resolve(JOIN_ENDPOINT), GSON.toJson(request), "application/json");
    }

    public CompletableFuture<HasJoinedResponse> getHasJoinedServer(GameProfile profile, String serverId) {
        URI uri = SESSIONS_URI.resolve(HAS_JOINED_ENDPOINT);
        try {
            return HttpUtils.createGetRequest(new URI(uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    "username=" + profile.getName() + "&serverId=" + serverId,
                    uri.getFragment())
            ).thenApply(response -> GSON.fromJson(response, HasJoinedResponse.class));
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to check if " + profile.getName() + " has joined a server!", ex);
        }
    }

    public void fillProperties(GameProfile profile) {
        if (profile.getId() == null) {
            return;
        }
        URI uri = SESSIONS_URI.resolve(PROFILE_ENDPOINT).resolve("/" + profile.getId().toString().replace("-", ""));
        try {
            HttpUtils.createGetRequest(new URI(uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    "unsigned=false",
                    uri.getFragment())
            ).whenComplete((response, ex) -> {
                ProfilePropertiesResponse propertiesResponse = GSON.fromJson(response, ProfilePropertiesResponse.class);
                profile.setProperties(propertiesResponse.getProperties());
            });
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to fetch profile properties from " + profile.getId(), ex);
        }
    }
}
