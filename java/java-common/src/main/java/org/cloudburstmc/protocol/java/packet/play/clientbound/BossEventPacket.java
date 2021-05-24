package org.cloudburstmc.protocol.java.packet.play.clientbound;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.data.entity.boss.BarAction;
import org.cloudburstmc.protocol.java.data.entity.boss.BarColor;
import org.cloudburstmc.protocol.java.data.entity.boss.BarDivision;
import org.cloudburstmc.protocol.java.data.entity.boss.BarFlag;
import org.cloudburstmc.protocol.java.handler.JavaPlayPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaPlayPacketType;

import java.util.Set;
import java.util.UUID;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class BossEventPacket extends JavaPacket<JavaPlayPacketHandler> {
    private UUID uuid;
    private BarAction action;
    private Component title;
    private float health;
    private BarColor color;
    private BarDivision division;
    private Set<BarFlag> flags;

    @Override
    public boolean handle(JavaPlayPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaPlayPacketType.BOSS_EVENT_S2C;
    }
}
