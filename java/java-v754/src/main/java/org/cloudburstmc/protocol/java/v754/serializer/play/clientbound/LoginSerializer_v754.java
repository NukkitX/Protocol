package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.key.Key;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.GameType;
import org.cloudburstmc.protocol.java.packet.play.clientbound.LoginPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginSerializer_v754 implements JavaPacketSerializer<LoginPacket> {
    public static final LoginSerializer_v754 INSTANCE = new LoginSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, LoginPacket packet) throws PacketSerializeException {
        buffer.writeInt(packet.getEntityId());
        buffer.writeBoolean(packet.isHardcore());
        buffer.writeByte(packet.getGameType().ordinal());
        buffer.writeByte(packet.getPreviousGameType().ordinal());
        helper.writeArray(buffer, packet.getDimensions(), helper::writeKey);
        helper.writeTag(buffer, packet.getDimensionCodec());
        helper.writeTag(buffer, packet.getDimension());
        helper.writeKey(buffer, packet.getDimensionName());
        buffer.writeLong(packet.getSeedHash());
        VarInts.writeUnsignedInt(buffer, packet.getMaxPlayers());
        VarInts.writeUnsignedInt(buffer, packet.getChunkRadius());
        buffer.writeBoolean(packet.isReducedDebugInfo());
        buffer.writeBoolean(packet.isEnableRespawnScreen());
        buffer.writeBoolean(packet.isDebug());
        buffer.writeBoolean(packet.isFlat());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, LoginPacket packet) throws PacketSerializeException {
        packet.setEntityId(buffer.readInt());
        packet.setHardcore(buffer.readBoolean());
        packet.setGameType(GameType.getById(buffer.readUnsignedByte()));
        packet.setPreviousGameType(GameType.getById(buffer.readUnsignedByte()));
        packet.setDimensions(helper.readArray(buffer, new Key[0], helper::readKey));
        packet.setDimensionCodec(helper.readTag(buffer));
        packet.setDimension(helper.readTag(buffer));
        packet.setDimensionName(helper.readKey(buffer));
        packet.setSeedHash(buffer.readLong());
        packet.setMaxPlayers(VarInts.readUnsignedInt(buffer));
        packet.setChunkRadius(VarInts.readUnsignedInt(buffer));
        packet.setReducedDebugInfo(buffer.readBoolean());
        packet.setEnableRespawnScreen(buffer.readBoolean());
        packet.setDebug(buffer.readBoolean());
        packet.setFlat(buffer.readBoolean());
    }
}
