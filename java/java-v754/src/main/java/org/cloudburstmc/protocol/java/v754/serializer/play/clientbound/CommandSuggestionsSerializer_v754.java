package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.CommandSuggestionsPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommandSuggestionsSerializer_v754 implements JavaPacketSerializer<CommandSuggestionsPacket> {
    public static final CommandSuggestionsSerializer_v754 INSTANCE = new CommandSuggestionsSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, CommandSuggestionsPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getTransactionId());
        VarInts.writeUnsignedInt(buffer, packet.getStart());
        VarInts.writeUnsignedInt(buffer, packet.getLength());
        VarInts.writeUnsignedInt(buffer, packet.getMatches().length);
        for (int index = 0; index < packet.getMatches().length; index++) {
            helper.writeString(buffer, packet.getMatches()[index]);
            Component tooltip = packet.getTooltips()[index];
            if (tooltip != null) {
                buffer.writeBoolean(true);
                helper.writeString(buffer, GsonComponentSerializer.gson().serialize(tooltip));
            } else {
                buffer.writeBoolean(false);
            }
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, CommandSuggestionsPacket packet) {
        packet.setTransactionId(VarInts.readUnsignedInt(buffer));
        packet.setStart(VarInts.readUnsignedInt(buffer));
        packet.setLength(VarInts.readUnsignedInt(buffer));
        packet.setMatches(new String[VarInts.readUnsignedInt(buffer)]);
        packet.setTooltips(new Component[packet.getMatches().length]);
        String[] matches = packet.getMatches();
        Component[] tooltips = packet.getTooltips();
        for (int index = 0; index < packet.getMatches().length; index++) {
            matches[index] = helper.readString(buffer);
            packet.setMatches(matches);
            if (buffer.readBoolean()) {
                tooltips[index] = GsonComponentSerializer.gson().deserialize(helper.readString(buffer));
                packet.setTooltips(tooltips);
            }
        }
    }
}
