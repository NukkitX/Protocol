package org.cloudburstmc.protocol.java.packet.play.serverbound;

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
public abstract class MovePlayerPacket extends JavaPacket<JavaPlayPacketHandler> {
    private boolean onGround;

    @Data
    @EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
    public static class Pos extends MovePlayerPacket {
        private Vector3d position;

        @Override
        public boolean handle(JavaPlayPacketHandler handler) {
            return handler.handle(this);
        }

        @Override
        public JavaPacketType getPacketType() {
            return JavaPlayPacketType.MOVE_PLAYER_POS_C2S;
        }
    }

    @Data
    @EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
    public static class PosRot extends MovePlayerPacket {
        private Vector3d position;
        private Vector2f rotation;

        @Override
        public boolean handle(JavaPlayPacketHandler handler) {
            return handler.handle(this);
        }

        @Override
        public JavaPacketType getPacketType() {
            return JavaPlayPacketType.MOVE_PLAYER_POS_C2S;
        }
    }

    @Data
    @EqualsAndHashCode(doNotUseGetters = true, callSuper = true)
    public static class Rot extends MovePlayerPacket {
        private Vector2f rotation;

        @Override
        public boolean handle(JavaPlayPacketHandler handler) {
            return handler.handle(this);
        }

        @Override
        public JavaPacketType getPacketType() {
            return JavaPlayPacketType.MOVE_PLAYER_POS_C2S;
        }
    }
}
