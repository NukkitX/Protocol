package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.world.BlockBreakStatus;
import org.cloudburstmc.protocol.java.packet.play.clientbound.BlockBreakAckPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlockBreakAckSerializer_v754 implements JavaPacketSerializer<BlockBreakAckPacket> {
    public static final BlockBreakAckSerializer_v754 INSTANCE = new BlockBreakAckSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, BlockBreakAckPacket packet) {
        helper.writeBlockPosition(buffer, packet.getPosition());
        VarInts.writeUnsignedInt(buffer, packet.getBlockState());
        VarInts.writeUnsignedInt(buffer, packet.getStatus().ordinal());
        buffer.writeBoolean(packet.isSuccessful());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, BlockBreakAckPacket packet) {
        packet.setPosition(helper.readBlockPosition(buffer));
        packet.setBlockState(VarInts.readUnsignedInt(buffer));
        packet.setStatus(BlockBreakStatus.getById(VarInts.readUnsignedInt(buffer)));
        packet.setSuccessful(buffer.readBoolean());
    }
}
