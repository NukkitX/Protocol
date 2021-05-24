package org.cloudburstmc.protocol.java.v754.serializer.login;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;
import org.cloudburstmc.protocol.java.packet.login.LoginStartPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginStartSerializer_v754 implements JavaPacketSerializer<LoginStartPacket> {
    public static final LoginStartSerializer_v754 INSTANCE = new LoginStartSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, LoginStartPacket packet) {
        helper.writeString(buffer, packet.getProfile().getName());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, LoginStartPacket packet) {
        packet.setProfile(new GameProfile(null, helper.readString(buffer)));
    }
}
