package org.cloudburstmc.protocol.java.v754.serializer.login;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.login.CustomQueryRequestPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomQueryRequestSerializer_v754 implements JavaPacketSerializer<CustomQueryRequestPacket> {
    public static final CustomQueryRequestSerializer_v754 INSTANCE = new CustomQueryRequestSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, CustomQueryRequestPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getTransactionId());
        helper.writeKey(buffer, packet.getIdentifier());
        helper.writeByteArray(buffer, packet.getData());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, CustomQueryRequestPacket packet) {
        packet.setTransactionId(VarInts.readUnsignedInt(buffer));
        packet.setIdentifier(helper.readKey(buffer));
        packet.setData(helper.readByteArray(buffer));
    }
}
