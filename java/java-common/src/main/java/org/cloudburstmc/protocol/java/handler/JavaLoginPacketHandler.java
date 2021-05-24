package org.cloudburstmc.protocol.java.handler;

import org.cloudburstmc.protocol.java.packet.login.*;

public interface JavaLoginPacketHandler extends JavaPacketHandler {

    default boolean handle(DisconnectPacket packet) {
        return false;
    }

    default boolean handle(EncryptionRequestPacket packet) {
        return false;
    }

    default boolean handle(LoginSuccessPacket packet) {
        return false;
    }

    default boolean handle(SetCompressionPacket packet) {
        return false;
    }

    default boolean handle(CustomQueryRequestPacket packet) {
        return false;
    }

    default boolean handle(LoginStartPacket packet) {
        return false;
    }

    default boolean handle(EncryptionResponsePacket packet) {
        return false;
    }

    default boolean handle(CustomQueryResponsePacket packet) {
        return false;
    }
}
