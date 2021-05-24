package org.cloudburstmc.protocol.java.packet.login;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.handler.JavaLoginPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaLoginPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;

import java.security.PublicKey;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class EncryptionRequestPacket extends JavaPacket<JavaLoginPacketHandler> {
    private PublicKey publicKey;
    private byte[] verifyToken;

    @Override
    public boolean handle(JavaLoginPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaLoginPacketType.ENCRYPTION_REQUEST;
    }
}
