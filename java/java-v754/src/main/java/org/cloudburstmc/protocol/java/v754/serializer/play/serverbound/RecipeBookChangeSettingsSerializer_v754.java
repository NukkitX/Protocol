package org.cloudburstmc.protocol.java.v754.serializer.play.serverbound;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.RecipeBookType;
import org.cloudburstmc.protocol.java.packet.play.serverbound.RecipeBookChangeSettingsPacket;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RecipeBookChangeSettingsSerializer_v754 implements JavaPacketSerializer<RecipeBookChangeSettingsPacket> {
    public static final RecipeBookChangeSettingsSerializer_v754 INSTANCE = new RecipeBookChangeSettingsSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, RecipeBookChangeSettingsPacket packet)
            throws PacketSerializeException {
        helper.writeVarInt(buffer, packet.getBookType().ordinal());
        buffer.writeBoolean(packet.isOpen());
        buffer.writeBoolean(packet.isFiltering());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, RecipeBookChangeSettingsPacket packet)
            throws PacketSerializeException {
        packet.setBookType(RecipeBookType.getById(helper.readVarInt(buffer)));
        packet.setOpen(buffer.readBoolean());
        packet.setFiltering(buffer.readBoolean());
    }
}
