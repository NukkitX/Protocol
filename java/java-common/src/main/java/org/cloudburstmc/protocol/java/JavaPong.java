package org.cloudburstmc.protocol.java;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;

@Data
@NoArgsConstructor
public class JavaPong {
    private Component description;

    private Players players;
    private Version version;

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Players {
        private final int max;
        private final int online;
        GameProfile[] sample = null;
    }

    @Data
    @AllArgsConstructor
    public static class Version {
        private String name;
        private int protocol;
    }
}
