package com.nukkitx.protocol.bedrock.v291.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.packet.MoveEntityDeltaPacket;
import com.nukkitx.protocol.util.TriConsumer;
import io.netty.buffer.ByteBuf;

import java.util.EnumMap;
import java.util.Set;

public class MoveEntityDeltaSerializer_v291 implements BedrockPacketSerializer<MoveEntityDeltaPacket> {
    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> READER_DELTA_X =
            (buffer, helper, packet) -> packet.setDeltaX(VarInts.readInt(buffer));
    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> READER_DELTA_Y =
            (buffer, helper, packet) -> packet.setDeltaY(VarInts.readInt(buffer));
    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> READER_DELTA_Z =
            (buffer, helper, packet) -> packet.setDeltaZ(VarInts.readInt(buffer));

    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> READER_PITCH =
            (buffer, helper, packet) -> packet.setPitch(helper.readByteAngle(buffer));
    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> READER_YAW =
            (buffer, helper, packet) -> packet.setYaw(helper.readByteAngle(buffer));
    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> READER_HEAD_YAW =
            (buffer, helper, packet) -> packet.setHeadYaw(helper.readByteAngle(buffer));

    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> WRITER_DELTA_X =
            (buffer, helper, packet) -> VarInts.writeInt(buffer, packet.getDeltaX());
    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> WRITER_DELTA_Y =
            (buffer, helper, packet) -> VarInts.writeInt(buffer, packet.getDeltaY());
    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> WRITER_DELTA_Z =
            (buffer, helper, packet) -> VarInts.writeInt(buffer, packet.getDeltaZ());

    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> WRITER_PITCH =
            (buffer, helper, packet) -> helper.writeByteAngle(buffer, packet.getPitch());
    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> WRITER_YAW =
            (buffer, helper, packet) -> helper.writeByteAngle(buffer, packet.getYaw());
    protected static final TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> WRITER_HEAD_YAW =
            (buffer, helper, packet) -> helper.writeByteAngle(buffer, packet.getHeadYaw());

    protected static final MoveEntityDeltaPacket.Flag[] FLAGS = MoveEntityDeltaPacket.Flag.values();

    public static final MoveEntityDeltaSerializer_v291 INSTANCE = new MoveEntityDeltaSerializer_v291();

    protected final EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket>> readers = new EnumMap<>(MoveEntityDeltaPacket.Flag.class);
    protected final EnumMap<MoveEntityDeltaPacket.Flag, TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket>> writers = new EnumMap<>(MoveEntityDeltaPacket.Flag.class);

    protected MoveEntityDeltaSerializer_v291() {
        this.readers.put(MoveEntityDeltaPacket.Flag.HAS_X, READER_DELTA_X);
        this.readers.put(MoveEntityDeltaPacket.Flag.HAS_Y, READER_DELTA_Y);
        this.readers.put(MoveEntityDeltaPacket.Flag.HAS_Z, READER_DELTA_Z);
        this.readers.put(MoveEntityDeltaPacket.Flag.HAS_PITCH, READER_PITCH);
        this.readers.put(MoveEntityDeltaPacket.Flag.HAS_YAW, READER_YAW);
        this.readers.put(MoveEntityDeltaPacket.Flag.HAS_HEAD_YAW, READER_HEAD_YAW);

        this.writers.put(MoveEntityDeltaPacket.Flag.HAS_X, WRITER_DELTA_X);
        this.writers.put(MoveEntityDeltaPacket.Flag.HAS_Y, WRITER_DELTA_Y);
        this.writers.put(MoveEntityDeltaPacket.Flag.HAS_Z, WRITER_DELTA_Z);
        this.writers.put(MoveEntityDeltaPacket.Flag.HAS_PITCH, WRITER_PITCH);
        this.writers.put(MoveEntityDeltaPacket.Flag.HAS_YAW, WRITER_YAW);
        this.writers.put(MoveEntityDeltaPacket.Flag.HAS_HEAD_YAW, WRITER_HEAD_YAW);
    }

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, MoveEntityDeltaPacket packet) {
        VarInts.writeUnsignedLong(buffer, packet.getRuntimeEntityId());

        int flagsIndex = buffer.writerIndex();
        buffer.writeByte(0); // flags

        int flags = 0;
        for (MoveEntityDeltaPacket.Flag flag : packet.getFlags()) {
            flags |= 1 << flag.ordinal();

            TriConsumer<ByteBuf, BedrockPacketHelper, MoveEntityDeltaPacket> writer = this.writers.get(flag);
            if (writer != null) {
                writer.accept(buffer, helper, packet);
            }
        }

        // Go back to flags and set them
        int currentIndex = buffer.writerIndex();
        buffer.writerIndex(flagsIndex);
        buffer.writeByte(flags);
        buffer.writerIndex(currentIndex);
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, MoveEntityDeltaPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        int flags = buffer.readUnsignedByte();
        Set<MoveEntityDeltaPacket.Flag> flagSet = packet.getFlags();

        for (MoveEntityDeltaPacket.Flag flag : FLAGS) {
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
