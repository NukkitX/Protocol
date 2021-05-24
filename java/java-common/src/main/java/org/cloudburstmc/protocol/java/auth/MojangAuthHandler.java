package org.cloudburstmc.protocol.java.auth;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import lombok.experimental.UtilityClass;
import org.cloudburstmc.protocol.java.JavaClientSession;
import org.cloudburstmc.protocol.java.data.auth.AuthData;
import org.cloudburstmc.protocol.java.data.auth.mojang.AuthRefreshResponse;
import org.cloudburstmc.protocol.java.data.auth.mojang.AuthRequest;
import org.cloudburstmc.protocol.java.data.auth.mojang.InvalidateRequest;
import org.cloudburstmc.protocol.java.data.auth.mojang.RefreshRequest;
import org.cloudburstmc.protocol.java.exception.AuthException;
import org.cloudburstmc.protocol.java.util.HttpUtils;

import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.cloudburstmc.protocol.java.Java.GSON;

@UtilityClass
public class MojangAuthHandler {
    private static final URI AUTH_URI = URI.create("https://authserver.mojang.com/");
    private static final String AUTHENTICATE_ENDPOINT = "authenticate";
    private static final String REFRESH_ENDPOINT = "refresh";
    private static final String INVALIDATE_ENDPOINT = "invalidate";

    private static final AuthRequest.Agent MINECRAFT = new AuthRequest.Agent("Minecraft", 1);

    public static CompletableFuture<AuthRefreshResponse> login(JavaClientSession session, AuthData authData) {
        Preconditions.checkNotNull(authData, "AuthData cannot be null");
        boolean token = authData.getAccessToken() != null;
        boolean password = authData.getPassword() != null;
        if (!token && !password) {
            throw new IllegalArgumentException("Access token and password were null!");
        }

        CompletableFuture<JsonElement> response;
        if (token) {
            RefreshRequest request = new RefreshRequest(authData.getClientToken(), authData.getClientToken(), null, true);
            response = HttpUtils.createPostRequest(AUTH_URI.resolve(REFRESH_ENDPOINT), GSON.toJson(request), "application/json");
        } else {
            AuthRequest request = new AuthRequest(MINECRAFT, authData.getUsername(), authData.getPassword(), authData.getClientToken(), true);
            response = HttpUtils.createPostRequest(AUTH_URI.resolve(AUTHENTICATE_ENDPOINT), GSON.toJson(request), "application/json");
        }
        return response.thenApply(element -> GSON.fromJson(element, AuthRefreshResponse.class)).whenComplete(((authResponse, throwable) -> {
            try {
                if (authResponse == null) {
                    throw new CompletionException(new AuthException("Response was invalid"));
                }
                if (!authResponse.getClientToken().equals(authData.getClientToken()) && authData.getClientToken() != null) {
                    throw new CompletionException(new AuthException("Client token did not match up with server"));
                }

                authData.setAccessToken(authResponse.getAccessToken());
                authData.setClientToken(authResponse.getClientToken());
                session.setProfile(authResponse.getSelectedProfile());
                session.getPropertyMap().clear();
                if (authResponse.getUser() != null && authResponse.getUser().getProperties() != null) {
                    session.getPropertyMap().putAll(authResponse.getUser().getProperties());
                }
                session.setLoggedIn(true);
            } finally {
                // Always clear credentials
                authData.setUsername(null);
                authData.setPassword(null);
            }
        }));
    }

    public void logout(JavaClientSession session) {
        Preconditions.checkState(session.isLoggedIn(), "You are not logged in!");

        InvalidateRequest request = new InvalidateRequest(session.getAuthData().getClientToken(), session.getAuthData().getAccessToken());
        HttpUtils.createPostRequest(AUTH_URI.resolve(INVALIDATE_ENDPOINT), GSON.toJson(request), "application/json");

        session.getAuthData().setAccessToken(null);
        session.getAuthData().setClientToken(null);
        session.getPropertyMap().clear();
        session.getProfiles().clear();
        session.setProfile(null);
    }
}
