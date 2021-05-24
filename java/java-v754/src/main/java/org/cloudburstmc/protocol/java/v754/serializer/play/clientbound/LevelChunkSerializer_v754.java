package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.LevelChunkPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LevelChunkSerializer_v754 implements JavaPacketSerializer<LevelChunkPacket> {
    public static final LevelChunkSerializer_v754 INSTANCE = new LevelChunkSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, LevelChunkPacket packet) throws PacketSerializeException {
        buffer.writeInt(packet.getX());
        buffer.writeInt(packet.getZ());
        buffer.writeBoolean(packet.isFullChunk());
        VarInts.writeUnsignedInt(buffer, packet.getAvailableSections());
        helper.writeTag(buffer, packet.getHeightmaps());
        if (packet.getBiomes() != null) {
            VarInts.writeUnsignedInt(buffer, packet.getBiomes().length);
            for (int biome : packet.getBiomes()) {
                VarInts.writeUnsignedInt(buffer, biome);
            }
        }
        helper.writeByteArray(buffer, packet.getBuffer());
        helper.writeArray(buffer, packet.getBlockEntities(), helper::writeTag);
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, LevelChunkPacket packet) throws PacketSerializeException {
        packet.setX(buffer.readInt());
        packet.setZ(buffer.readInt());
        packet.setFullChunk(buffer.readBoolean());
        packet.setAvailableSections(VarInts.readUnsignedInt(buffer));
        packet.setHeightmaps(helper.readTag(buffer));
        if (packet.isFullChunk()) {
            int[] biomes = new int[VarInts.readUnsignedInt(buffer)];
            for (int i = 0; i < biomes.length; i++) {
                biomes[i] = VarInts.readUnsignedInt(buffer);
            }
            packet.setBiomes(biomes);
        }
        int bufferSize = VarInts.readUnsignedInt(buffer);
        if (bufferSize > 2097152) { // Cap at 2MB - don't want people crashing us :(
            throw new RuntimeException("Chunk packet too big!");
        }
        byte[] chunkBuffer = new byte[bufferSize];
        buffer.readBytes(chunkBuffer);
        packet.setBuffer(chunkBuffer);
        helper.readArray(buffer, packet.getBlockEntities(), helper::readTag);
    }
}
