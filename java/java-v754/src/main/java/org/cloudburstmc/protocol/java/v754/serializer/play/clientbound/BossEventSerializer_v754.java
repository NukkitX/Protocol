package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.entity.boss.BarAction;
import org.cloudburstmc.protocol.java.data.entity.boss.BarColor;
import org.cloudburstmc.protocol.java.data.entity.boss.BarDivision;
import org.cloudburstmc.protocol.java.data.entity.boss.BarFlag;
import org.cloudburstmc.protocol.java.packet.play.clientbound.BossEventPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BossEventSerializer_v754 implements JavaPacketSerializer<BossEventPacket> {
    public static final BossEventSerializer_v754 INSTANCE = new BossEventSerializer_v754();

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, BossEventPacket packet) {
        helper.writeUUID(buffer, packet.getUuid());
        VarInts.writeUnsignedInt(buffer, packet.getAction().ordinal());
        switch (packet.getAction()) {
            case ADD:
                helper.writeString(buffer, GsonComponentSerializer.gson().serialize(packet.getTitle()));
                buffer.writeFloat(packet.getHealth());
                VarInts.writeUnsignedInt(buffer, packet.getColor().ordinal());
                VarInts.writeUnsignedInt(buffer, packet.getDivision().ordinal());
                int flags = 0;
                for (BarFlag flag : packet.getFlags()) {
                    flags |= flag.getBitMask();
                }
                buffer.writeByte(flags);
                break;
            case UPDATE_HEALTH:
                buffer.writeFloat(packet.getHealth());
                break;
            case UPDATE_TITLE:
                helper.writeString(buffer, GsonComponentSerializer.gson().serialize(packet.getTitle()));
                break;
            case UPDATE_STYLE:
                VarInts.writeUnsignedInt(buffer, packet.getColor().ordinal());
                VarInts.writeUnsignedInt(buffer, packet.getDivision().ordinal());
                break;
            case UPDATE_FLAGS:
                flags = 0;
                for (BarFlag flag : packet.getFlags()) {
                    flags |= flag.getBitMask();
                }
                buffer.writeByte(flags);
                break;
        }
    }

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, BossEventPacket packet) {
        packet.setUuid(helper.readUUID(buffer));
        BarAction action = BarAction.getById(VarInts.readUnsignedInt(buffer));
        packet.setAction(action);
        switch (action) {
            case ADD:
                packet.setTitle(GsonComponentSerializer.gson().deserialize(helper.readString(buffer)));
                packet.setHealth(buffer.readFloat());
                packet.setColor(BarColor.getById(VarInts.readUnsignedInt(buffer)));
                packet.setDivision(BarDivision.getById(VarInts.readUnsignedInt(buffer)));
                int flags = buffer.readUnsignedByte();
                for (BarFlag flag : BarFlag.VALUES) {
                    if ((flags & flag.getBitMask()) == flag.getBitMask()) {
                        packet.getFlags().add(flag);
                    }
                }
                break;
            case UPDATE_HEALTH:
                packet.setHealth(buffer.readFloat());
                break;
            case UPDATE_TITLE:
                packet.setTitle(GsonComponentSerializer.gson().deserialize(helper.readString(buffer)));
                break;
            case UPDATE_STYLE:
                packet.setColor(BarColor.getById(VarInts.readUnsignedInt(buffer)));
                packet.setDivision(BarDivision.getById(VarInts.readUnsignedInt(buffer)));
                break;
            case UPDATE_FLAGS:
                flags = buffer.readUnsignedByte();
                for (BarFlag flag : BarFlag.VALUES) {
                    if ((flags & flag.getBitMask()) == flag.getBitMask()) {
                        packet.getFlags().add(flag);
                    }
                }
                break;
        }
    }
}
