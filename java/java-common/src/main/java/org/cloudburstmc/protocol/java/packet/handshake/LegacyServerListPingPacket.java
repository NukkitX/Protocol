package org.cloudburstmc.protocol.java.packet.handshake;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.handler.JavaHandshakePacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaHandshakePacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class LegacyServerListPingPacket extends JavaPacket<JavaHandshakePacketHandler> {
    private byte payload;

    @Override
    public boolean handle(JavaHandshakePacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaHandshakePacketType.LEGACY_SERVER_LIST_PING;
    }
}
