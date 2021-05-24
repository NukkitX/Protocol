package org.cloudburstmc.protocol.java.packet.play.clientbound;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.handler.JavaPlayPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaPlayPacketType;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class SetObjectivePacket extends JavaPacket<JavaPlayPacketHandler> {
    private String objectiveName;
    private Action action;
    private Component displayName;
    private RenderType renderType;

    @Override
    public boolean handle(JavaPlayPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaPlayPacketType.SET_OBJECTIVE_S2C;
    }

    public enum RenderType {
        INTEGER,
        HEARTS;

        private static final RenderType[] VALUES = values();

        public static RenderType getById(int id) {
            return VALUES.length > id ? VALUES[id] : null;
        }
    }

    public enum Action {
        ADD,
        REMOVE,
        UPDATE;

        private static final Action[] VALUES = values();

        public static Action getById(int id) {
            return VALUES.length > id ? VALUES[id] : null;
        }
    }
}
