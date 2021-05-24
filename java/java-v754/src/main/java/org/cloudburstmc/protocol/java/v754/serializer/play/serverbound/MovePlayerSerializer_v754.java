package org.cloudburstmc.protocol.java.v754.serializer.play.serverbound;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.serverbound.MovePlayerPacket;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MovePlayerSerializer_v754<T extends MovePlayerPacket> implements JavaPacketSerializer<T> {

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Pos extends MovePlayerSerializer_v754<MovePlayerPacket.Pos> {
        public static final Pos INSTANCE = new Pos();

        @Override
        public void serialize(ByteBuf buffer, JavaPacketHelper helper, MovePlayerPacket.Pos packet)
                throws PacketSerializeException {
            helper.writePosition(buffer, packet.getPosition());
            buffer.writeBoolean(packet.isOnGround());
        }

        @Override
        public void deserialize(ByteBuf buffer, JavaPacketHelper helper, MovePlayerPacket.Pos packet)
                throws PacketSerializeException {
            packet.setPosition(helper.readPosition(buffer));
            packet.setOnGround(buffer.readBoolean());
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class PosRot extends MovePlayerSerializer_v754<MovePlayerPacket.PosRot> {
        public static final PosRot INSTANCE = new PosRot();

        @Override
        public void serialize(ByteBuf buffer, JavaPacketHelper helper, MovePlayerPacket.PosRot packet)
                throws PacketSerializeException {
            helper.writePosition(buffer, packet.getPosition());
            buffer.writeFloat(packet.getRotation().getX());
            buffer.writeFloat(packet.getRotation().getY());
            buffer.writeBoolean(packet.isOnGround());
        }

        @Override
        public void deserialize(ByteBuf buffer, JavaPacketHelper helper, MovePlayerPacket.PosRot packet)
                throws PacketSerializeException {
            packet.setPosition(helper.readPosition(buffer));
            packet.setRotation(Vector2f.from(buffer.readFloat(), buffer.readFloat()));
            packet.setOnGround(buffer.readBoolean());
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Rot extends MovePlayerSerializer_v754<MovePlayerPacket.Rot> {
        public static final Rot INSTANCE = new Rot();

        @Override
        public void serialize(ByteBuf buffer, JavaPacketHelper helper, MovePlayerPacket.Rot packet)
                throws PacketSerializeException {
            buffer.writeFloat(packet.getRotation().getX());
            buffer.writeFloat(packet.getRotation().getY());
            buffer.writeBoolean(packet.isOnGround());
        }

        @Override
        public void deserialize(ByteBuf buffer, JavaPacketHelper helper, MovePlayerPacket.Rot packet)
                throws PacketSerializeException {
            packet.setRotation(Vector2f.from(buffer.readFloat(), buffer.readFloat()));
            packet.setOnGround(buffer.readBoolean());
        }
    }
}
