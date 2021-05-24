package org.cloudburstmc.protocol.java.v754.serializer.login;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.login.CustomQueryResponsePacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomQueryResponseSerializer_v754 implements JavaPacketSerializer<CustomQueryResponsePacket> {
    public static final CustomQueryResponseSerializer_v754 INSTANCE = new CustomQueryResponseSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, CustomQueryResponsePacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getTransactionId());
        buffer.writeBoolean(packet.isSuccessful());
        if (packet.isSuccessful()) {
            helper.writeByteArray(buffer, packet.getData());
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, CustomQueryResponsePacket packet) {
        packet.setTransactionId(VarInts.readUnsignedInt(buffer));
        boolean successful = buffer.readBoolean();
        packet.setSuccessful(successful);
        if (successful) {
            packet.setData(helper.readByteArray(buffer));
        }
    }
}
