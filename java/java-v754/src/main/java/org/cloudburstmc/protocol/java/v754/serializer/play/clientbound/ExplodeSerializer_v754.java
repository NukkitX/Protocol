package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.math.vector.Vector3d;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.ExplodePacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExplodeSerializer_v754 implements JavaPacketSerializer<ExplodePacket> {
    public static ExplodeSerializer_v754 INSTANCE = new ExplodeSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, ExplodePacket packet) {
        buffer.writeFloat((float) packet.getCenter().getX());
        buffer.writeFloat((float) packet.getCenter().getY());
        buffer.writeFloat((float) packet.getCenter().getZ());
        buffer.writeFloat(packet.getPower());
        Vector3i flooredCenter = packet.getCenter().toInt();
        buffer.writeInt(packet.getToBlow().size());
        for (Vector3i toBlow : packet.getToBlow()) {
            buffer.writeByte(toBlow.getX() - flooredCenter.getX());
            buffer.writeByte(toBlow.getY() - flooredCenter.getY());
            buffer.writeByte(toBlow.getZ() - flooredCenter.getZ());
        }
        buffer.writeFloat(packet.getKnockback().getX());
        buffer.writeFloat(packet.getKnockback().getY());
        buffer.writeFloat(packet.getKnockback().getZ());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, ExplodePacket packet) {
        packet.setCenter(Vector3d.from(
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat()
        ));
        packet.setPower(buffer.readFloat());
        Vector3i flooredCenter = packet.getCenter().toInt();
        int toBlowCount = buffer.readInt();
        for (int i = 0; i < toBlowCount; i++) {
            packet.getToBlow().add(Vector3i.from(
                    buffer.readByte() + flooredCenter.getX(),
                    buffer.readByte() + flooredCenter.getY(),
                    buffer.readByte() + flooredCenter.getZ()
            ));
        }
        packet.setKnockback(Vector3f.from(
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readFloat()
        ));
    }
}
