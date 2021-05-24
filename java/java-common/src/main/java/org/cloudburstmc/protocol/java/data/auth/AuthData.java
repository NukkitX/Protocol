package org.cloudburstmc.protocol.java.data.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthData {
    private String username;
    private String password;

    private String clientToken;
    private String accessToken;

    public AuthData(String username) {
        this.username = username;
    }

    public AuthData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthData(String username, String clientToken, String accessToken) {
        this.username = username;
        this.clientToken = clientToken;
        this.accessToken = accessToken;
    }
}
