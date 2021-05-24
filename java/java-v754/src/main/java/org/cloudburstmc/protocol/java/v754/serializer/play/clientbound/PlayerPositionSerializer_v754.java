package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.PositionFlag;
import org.cloudburstmc.protocol.java.packet.play.clientbound.PlayerPositionPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerPositionSerializer_v754 implements JavaPacketSerializer<PlayerPositionPacket> {
    public static final PlayerPositionSerializer_v754 INSTANCE = new PlayerPositionSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, PlayerPositionPacket packet) {
        helper.writePosition(buffer, packet.getPosition());
        buffer.writeFloat(packet.getRotation().getX());
        buffer.writeFloat(packet.getRotation().getY());
        int flags = 0;
        for (PositionFlag flag : packet.getFlags()) {
            flags |= flag.getBitMask();
        }
        buffer.writeByte(flags);
        VarInts.writeUnsignedInt(buffer, packet.getTeleportId());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, PlayerPositionPacket packet) {
        packet.setPosition(helper.readPosition(buffer));
        packet.setRotation(Vector2f.from(buffer.readFloat(), buffer.readFloat()));
        int flags = buffer.readUnsignedByte();
        for (PositionFlag flag : PositionFlag.VALUES) {
            if ((flags & flag.getBitMask()) == flag.getBitMask()) {
                packet.getFlags().add(flag);
            }
        }
        packet.setTeleportId(VarInts.readUnsignedInt(buffer));
    }
}
