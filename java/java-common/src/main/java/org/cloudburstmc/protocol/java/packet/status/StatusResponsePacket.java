package org.cloudburstmc.protocol.java.packet.status;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.JavaPong;
import org.cloudburstmc.protocol.java.handler.JavaStatusPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaStatusPacketType;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class StatusResponsePacket extends JavaPacket<JavaStatusPacketHandler> {
    private JavaPong response;

    @Override
    public boolean handle(JavaStatusPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaStatusPacketType.STATUS_RESPONSE;
    }
}
