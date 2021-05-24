package org.cloudburstmc.protocol.java.data.world;

import lombok.Value;

@Value
public class Particle {
    ParticleType type;
    Object data;

    @SuppressWarnings("unchecked")
    public <T extends Particle> T as() {
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends Particle> T as(Class<T> particleType) {
        return (T) this;
    }
}
