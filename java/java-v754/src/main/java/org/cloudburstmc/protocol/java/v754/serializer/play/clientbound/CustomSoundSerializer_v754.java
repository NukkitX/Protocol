package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.math.vector.Vector3d;
import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.CustomSoundPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomSoundSerializer_v754 implements JavaPacketSerializer<CustomSoundPacket> {
    public static final CustomSoundSerializer_v754 INSTANCE = new CustomSoundSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, CustomSoundPacket packet) {
        helper.writeString(buffer, packet.getName().asString());
        VarInts.writeUnsignedInt(buffer, packet.getSource().ordinal());
        buffer.writeInt((int) (packet.getPosition().getX() * 8));
        buffer.writeInt((int) (packet.getPosition().getY() * 8));
        buffer.writeInt((int) (packet.getPosition().getZ() * 8));
        buffer.writeFloat(packet.getVolume());
        buffer.writeFloat(packet.getPitch());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, CustomSoundPacket packet) {
        packet.setName(Key.key(helper.readString(buffer)));
        packet.setSource(Sound.Source.values()[VarInts.readUnsignedInt(buffer)]);
        packet.setPosition(Vector3d.from(
                buffer.readInt() / 8D,
                buffer.readInt() / 8D,
                buffer.readInt() / 8D
        ));
        packet.setVolume(buffer.readFloat());
        packet.setPitch(buffer.readFloat());
    }
}
