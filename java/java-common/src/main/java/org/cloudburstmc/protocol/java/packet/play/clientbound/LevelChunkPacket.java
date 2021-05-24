package org.cloudburstmc.protocol.java.packet.play.clientbound;

import com.nukkitx.nbt.NbtMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.handler.JavaPlayPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaPlayPacketType;

import java.util.List;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class LevelChunkPacket extends JavaPacket<JavaPlayPacketHandler> {
    private int x;
    private int z;
    private int availableSections;
    private NbtMap heightmaps;
    @Nullable
    private int[] biomes;
    private byte[] buffer;
    private final List<NbtMap> blockEntities = new ObjectArrayList<>();
    private boolean fullChunk;

    @Override
    public boolean handle(JavaPlayPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaPlayPacketType.LEVEL_CHUNK_S2C;
    }
}
