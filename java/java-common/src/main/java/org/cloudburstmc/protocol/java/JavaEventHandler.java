package org.cloudburstmc.protocol.java;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface JavaEventHandler<T extends JavaSession> {
    void onSessionCreation(T session);

    void onLogin(T session);
}