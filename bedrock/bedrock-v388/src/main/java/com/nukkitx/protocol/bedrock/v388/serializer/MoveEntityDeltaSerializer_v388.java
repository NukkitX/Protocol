package com.nukkitx.protocol.bedrock.v388.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.packet.MoveEntityDeltaPacket;
import com.nukkitx.protocol.bedrock.packet.MoveEntityDeltaPacket.Flag;
import com.nukkitx.protocol.util.TriConsumer;
import com.nukkitx.protocol.bedrock.v291.serializer.MoveEntityDeltaSerializer_v291;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MoveEntityDeltaSerializer_v388 extends MoveEntityDeltaSerializer_v291 {

    public static final MoveEntityDeltaSerializer_v388 INSTANCE = new MoveEntityDeltaSerializer_v388();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, MoveEntityDeltaPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());

        int flagsIndex = buffer.writerIndex();
        buffer.writeShortLE(0); // flags

        int flags = 0;
        for (Flag flag : packet.getFlags()) {
            flags |= 1 << flag.ordinal();

            TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> writer = this.writers.get(flag);
            if (writer != null) {
                writer.accept(buffer, helper, packet);
            }
        }

        // Go back to flags and set them
        int currentIndex = buffer.writerIndex();
        buffer.writerIndex(flagsIndex);
        buffer.writeShortLE(flags);
        buffer.writerIndex(currentIndex);
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, MoveEntityDeltaPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        int flags = buffer.readUnsignedShortLE();
        Set<Flag> flagSet = packet.getFlags();

        for (Flag flag : FLAGS) {
            if ((flags & (1 << flag.ordinal())) != 0) {
                flagSet.add(flag);
                TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> reader = this.readers.get(flag);
                if (reader != null) {
                    reader.accept(buffer, helper, packet);
                }
            }
        }
    }
}
