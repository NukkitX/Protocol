package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.MapDecoration;
import org.cloudburstmc.protocol.java.data.MapDecorationType;
import org.cloudburstmc.protocol.java.packet.play.clientbound.MapItemDataPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MapItemDataSerializer_v754 implements JavaPacketSerializer<MapItemDataPacket> {
    public static final MapItemDataSerializer_v754 INSTANCE = new MapItemDataSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, MapItemDataPacket packet) throws PacketSerializeException {
        helper.writeVarInt(buffer, packet.getId());
        buffer.writeByte(packet.getScale());
        buffer.writeBoolean(packet.isTrackingPosition());
        buffer.writeBoolean(packet.isLocked());
        helper.writeArray(buffer, packet.getDecorations(), (buf, decoration) -> {
            helper.writeVarInt(buffer, decoration.getType().ordinal());
            buffer.writeByte(decoration.getX());
            buffer.writeByte(decoration.getY());
            buffer.writeByte(decoration.getRotation() & 15);
            helper.writeOptional(buffer, decoration.getName(), helper::writeComponent);
        });
        buffer.writeByte(packet.getWidth());
        if (packet.getWidth() > 0) {
            buffer.writeByte(packet.getHeight());
            buffer.writeByte(packet.getStartX());
            buffer.writeByte(packet.getStartY());
            helper.writeByteArray(buffer, packet.getMapColors());
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, MapItemDataPacket packet) throws PacketSerializeException {
        packet.setId(helper.readVarInt(buffer));
        packet.setScale(buffer.readByte());
        packet.setTrackingPosition(buffer.readBoolean());
        packet.setLocked(buffer.readBoolean());
        packet.setDecorations(helper.readArray(buffer, new MapDecoration[0], (buf, decoration) ->
                new MapDecoration(
                        MapDecorationType.getById(helper.readVarInt(buffer)),
                        buffer.readByte(),
                        buffer.readByte(),
                        (byte) (buffer.readByte() & 15),
                        helper.readOptional(buffer, helper::readComponent)
                ))
        );
        packet.setWidth(buffer.readUnsignedByte());
        if (packet.getWidth() > 0) {
            packet.setHeight(buffer.readUnsignedByte());
            packet.setStartX(buffer.readUnsignedByte());
            packet.setStartY(buffer.readUnsignedByte());
            packet.setMapColors(helper.readByteArray(buffer));
        }
    }
}
