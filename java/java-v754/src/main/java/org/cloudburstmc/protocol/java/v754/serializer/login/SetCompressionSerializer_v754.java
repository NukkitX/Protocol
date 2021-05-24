package org.cloudburstmc.protocol.java.v754.serializer.login;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.login.SetCompressionPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SetCompressionSerializer_v754 implements JavaPacketSerializer<SetCompressionPacket> {
    public static final SetCompressionSerializer_v754 INSTANCE = new SetCompressionSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, SetCompressionPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getCompressionThreshold());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, SetCompressionPacket packet) {
        packet.setCompressionThreshold(VarInts.readUnsignedInt(buffer));
    }
}
