package org.cloudburstmc.protocol.java.v754.serializer.login;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.login.EncryptionResponsePacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EncryptionResponseSerializer_v754 implements JavaPacketSerializer<EncryptionResponsePacket> {
    public static final EncryptionResponseSerializer_v754 INSTANCE = new EncryptionResponseSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, EncryptionResponsePacket packet) {
        helper.writeByteArray(buffer, packet.getSharedSecret());
        helper.writeByteArray(buffer, packet.getVerifyToken());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, EncryptionResponsePacket packet) {
        packet.setSharedSecret(helper.readByteArray(buffer));
        packet.setVerifyToken(helper.readByteArray(buffer));
    }
}
