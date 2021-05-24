package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.BlockDestructionPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockDestructionSerializer_v754 implements JavaPacketSerializer<BlockDestructionPacket> {
    public static final BlockDestructionSerializer_v754 INSTANCE = new BlockDestructionSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, BlockDestructionPacket packet) {
        VarInts.writeUnsignedInt(buffer, packet.getEntityId());
        helper.writeBlockPosition(buffer, packet.getPosition());
        buffer.writeByte(packet.getDestroyStage());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, BlockDestructionPacket packet) {
        packet.setEntityId(VarInts.readUnsignedInt(buffer));
        packet.setPosition(helper.readBlockPosition(buffer));
        packet.setDestroyStage(buffer.readUnsignedByte());
    }
}
