package org.cloudburstmc.protocol.java.data.entity;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.util.TriConsumer;
import io.netty.buffer.ByteBuf;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.data.Direction;
import org.cloudburstmc.protocol.java.data.inventory.ItemStack;
import org.cloudburstmc.protocol.java.data.world.Particle;

import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EntityDataType<T> {
    public static final EntityDataType<Byte> BYTE = new EntityDataType<Byte>(ByteBuf::readByte, ByteBuf::writeByte);

    public static final EntityDataType<Integer> INT = new EntityDataType<>(VarInts::readUnsignedInt, VarInts::writeUnsignedInt);

    public static final EntityDataType<Float> FLOAT = new EntityDataType<>(ByteBuf::readFloat, ByteBuf::writeFloat);

    public static final EntityDataType<String> STRING = new EntityDataType<>(JavaPacketHelper::readString, JavaPacketHelper::writeString);

    public static final EntityDataType<Component> COMPONENT = new EntityDataType<>(JavaPacketHelper::readComponent, JavaPacketHelper::writeComponent);

    public static final EntityDataType<Optional<Component>> OPTIONAL_COMPONENT = new EntityDataType<>(readOptional(JavaPacketHelper::readComponent), writeOptional(JavaPacketHelper::writeComponent));

    public static final EntityDataType<ItemStack> ITEM_STACK = new EntityDataType<>(JavaPacketHelper::readItemStack, JavaPacketHelper::writeItemStack);

    public static final EntityDataType<Optional<Integer>> OPTIONAL_BLOCK_STATE = new EntityDataType<>(readOptional(JavaPacketHelper::readVarInt), writeOptional(JavaPacketHelper::writeVarInt));

    public static final EntityDataType<Boolean> BOOLEAN = new EntityDataType<>(ByteBuf::readBoolean, ByteBuf::writeBoolean);

    public static final EntityDataType<Particle> PARTICLE = new EntityDataType<>(JavaPacketHelper::readParticle, JavaPacketHelper::writeParticle);

    public static final EntityDataType<Vector3f> ROTATION = new EntityDataType<>(JavaPacketHelper::readRotation, JavaPacketHelper::writeRotation);

    public static final EntityDataType<Vector3i> BLOCK_POS = new EntityDataType<>(JavaPacketHelper::readBlockPosition, JavaPacketHelper::writeBlockPosition);

    public static final EntityDataType<Optional<Vector3i>> OPTIONAL_BLOCK_POS = new EntityDataType<>(readOptional(JavaPacketHelper::readBlockPosition), writeOptional(JavaPacketHelper::writeBlockPosition));

    public static final EntityDataType<Direction> DIRECTION = new EntityDataType<>(JavaPacketHelper::readDirection, JavaPacketHelper::writeDirection);

    public static final EntityDataType<Optional<UUID>> OPTIONAL_UUID = new EntityDataType<>(readOptional(JavaPacketHelper::readUUID), writeOptional(JavaPacketHelper::writeUUID));

    public static final EntityDataType<NbtMap> NBT = new EntityDataType<>(JavaPacketHelper::readTag, JavaPacketHelper::writeTag);

    public static final EntityDataType<VillagerData> VILLAGER_DATA = new EntityDataType<>(JavaPacketHelper::readVillagerData, JavaPacketHelper::writeVillagerData);

    public static final EntityDataType<Optional<Integer>> OPTIONAL_INT = new EntityDataType<>(readOptional(JavaPacketHelper::readVarInt), writeOptional(JavaPacketHelper::writeVarInt));

    public static final EntityDataType<Pose> POSE = new EntityDataType<>(JavaPacketHelper::readPose, JavaPacketHelper::writePose);

    private final BiFunction<JavaPacketHelper, ByteBuf, T> readFunction;
    private final TriConsumer<JavaPacketHelper, ByteBuf, T> writeFunction;

    public EntityDataType(Function<ByteBuf, T> readFunction, BiConsumer<ByteBuf, T> writeFunction) {
        this((helper, buf) -> readFunction.apply(buf), ((helper, buf, t) -> writeFunction.accept(buf, t)));
    }

    public EntityDataType(BiFunction<JavaPacketHelper, ByteBuf, T> readFunction, TriConsumer<JavaPacketHelper, ByteBuf, T> writeFunction) {
        this.readFunction = readFunction;
        this.writeFunction = writeFunction;
    }

    private static <E> BiFunction<JavaPacketHelper, ByteBuf, Optional<E>> readOptional(BiFunction<JavaPacketHelper, ByteBuf, E> readFunction) {
        return ((helper, buf) -> {
            if (!buf.readBoolean()) {
                return Optional.empty();
            }
            return Optional.of(readFunction.apply(helper, buf));
        });
    }

    private static <E> TriConsumer<JavaPacketHelper, ByteBuf, Optional<E>> writeOptional(TriConsumer<JavaPacketHelper, ByteBuf, E> writeFunction) {
        return ((helper, buf, e) -> {
            buf.writeBoolean(e.isPresent());
            e.ifPresent(value -> writeFunction.accept(helper, buf, value));
        });
    }

    public T read(JavaPacketHelper helper, ByteBuf buffer) {
        return this.readFunction.apply(helper, buffer);
    }

    public void write(JavaPacketHelper helper, ByteBuf buffer, T value) {
        this.writeFunction.accept(helper, buffer, value);
    }
}
