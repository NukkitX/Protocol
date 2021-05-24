package org.cloudburstmc.protocol.java.v754.serializer.play.serverbound;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.Direction;
import org.cloudburstmc.protocol.java.data.PlayerAction;
import org.cloudburstmc.protocol.java.packet.play.serverbound.PlayerActionPacket;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerActionSerializer_v754 implements JavaPacketSerializer<PlayerActionPacket> {
    public static PlayerActionSerializer_v754 INSTANCE = new PlayerActionSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, PlayerActionPacket packet)
            throws PacketSerializeException {
        helper.writeVarInt(buffer, packet.getAction().ordinal());
        helper.writeBlockPosition(buffer, packet.getPosition());
        buffer.writeByte(packet.getDirection().ordinal());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, PlayerActionPacket packet)
            throws PacketSerializeException {
        packet.setAction(PlayerAction.getById(helper.readVarInt(buffer)));
        packet.setPosition(helper.readBlockPosition(buffer));
        packet.setDirection(Direction.getById(buffer.readByte()));
    }
}
