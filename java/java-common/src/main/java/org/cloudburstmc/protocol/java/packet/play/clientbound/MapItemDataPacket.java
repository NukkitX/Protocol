package org.cloudburstmc.protocol.java.packet.play.clientbound;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.data.MapDecoration;
import org.cloudburstmc.protocol.java.handler.JavaPlayPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaPlayPacketType;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class MapItemDataPacket extends JavaPacket<JavaPlayPacketHandler> {
    private int id;
    private byte scale;
    private boolean trackingPosition;
    private boolean locked;
    private MapDecoration[] decorations;
    private int startX;
    private int startY;
    private int width;
    private int height;
    private byte[] mapColors;

    @Override
    public boolean handle(JavaPlayPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaPlayPacketType.MAP_ITEM_DATA_S2C;
    }
}
