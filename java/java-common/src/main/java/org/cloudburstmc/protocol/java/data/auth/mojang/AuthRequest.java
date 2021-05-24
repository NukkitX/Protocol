package org.cloudburstmc.protocol.java.data.auth.mojang;

import lombok.Value;

@Value
public class AuthRequest {
    Agent agent;
    String username;
    String password;
    String clientToken;
    boolean requestUser;

    @Value
    public static class Agent {
        String name;
        int version;
    }
}
