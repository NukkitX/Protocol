package org.cloudburstmc.protocol.java.v754.serializer.play.serverbound;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.SeenAdvancementsAction;
import org.cloudburstmc.protocol.java.packet.play.serverbound.SeenAdvancementsPacket;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SeenAdvancementsSerializer_v754 implements JavaPacketSerializer<SeenAdvancementsPacket> {
    public static final SeenAdvancementsSerializer_v754 INSTANCE = new SeenAdvancementsSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, SeenAdvancementsPacket packet)
            throws PacketSerializeException {
        helper.writeVarInt(buffer, packet.getAction().ordinal());
        if (packet.getAction() == SeenAdvancementsAction.OPENED_TAB) {
            helper.writeKey(buffer, packet.getKey());
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, SeenAdvancementsPacket packet)
            throws PacketSerializeException {
        packet.setAction(SeenAdvancementsAction.getById(helper.readVarInt(buffer)));
        if (packet.getAction() == SeenAdvancementsAction.OPENED_TAB) {
            packet.setKey(helper.readKey(buffer));
        }
    }
}
