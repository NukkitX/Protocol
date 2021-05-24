package org.cloudburstmc.protocol.java.packet.play.clientbound;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3d;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.handler.JavaPlayPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaPlayPacketType;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public abstract class MoveEntityPacket extends JavaPacket<JavaPlayPacketHandler> {
    private int entityId;
    private Vector3d delta = Vector3d.ZERO;
    private Vector2f rotation = Vector2f.ZERO;
    private boolean onGround;

    public abstract Type getType();

    public static class Pos extends MoveEntityPacket {

        @Override
        public boolean handle(JavaPlayPacketHandler handler) {
            return handler.handle(this);
        }

        @Override
        public Type getType() {
            return Type.POS;
        }

        @Override
        public JavaPacketType getPacketType() {
            return JavaPlayPacketType.MOVE_ENTITY_POS_S2C;
        }
    }

    public static class PosRot extends MoveEntityPacket {

        @Override
        public boolean handle(JavaPlayPacketHandler handler) {
            return handler.handle(this);
        }

        @Override
        public Type getType() {
            return Type.POS_ROT;
        }

        @Override
        public JavaPacketType getPacketType() {
            return JavaPlayPacketType.MOVE_ENTITY_POS_ROT_S2C;
        }
    }

    public static class Rot extends MoveEntityPacket {

        @Override
        public boolean handle(JavaPlayPacketHandler handler) {
            return handler.handle(this);
        }

        @Override
        public Type getType() {
            return Type.ROT;
        }

        @Override
        public JavaPacketType getPacketType() {
            return JavaPlayPacketType.MOVE_ENTITY_ROT_S2C;
        }
    }

    public enum Type {
        POS,
        POS_ROT,
        ROT;
    }
}
