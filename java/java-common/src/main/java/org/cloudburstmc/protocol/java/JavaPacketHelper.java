package org.cloudburstmc.protocol.java;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3d;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NBTOutputStream;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.network.VarInts;
import com.nukkitx.network.util.Preconditions;
import com.nukkitx.protocol.util.Int2ObjectBiMap;
import com.nukkitx.protocol.util.TriConsumer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.cloudburstmc.protocol.java.data.Direction;
import org.cloudburstmc.protocol.java.data.entity.EntityData;
import org.cloudburstmc.protocol.java.data.entity.EntityDataType;
import org.cloudburstmc.protocol.java.data.entity.EntityType;
import org.cloudburstmc.protocol.java.data.entity.Pose;
import org.cloudburstmc.protocol.java.data.entity.VillagerData;
import org.cloudburstmc.protocol.java.data.inventory.ItemStack;
import org.cloudburstmc.protocol.java.data.inventory.ContainerType;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;
import org.cloudburstmc.protocol.java.data.profile.property.Property;
import org.cloudburstmc.protocol.java.data.profile.property.PropertyMap;
import org.cloudburstmc.protocol.java.data.world.BlockEntityAction;
import org.cloudburstmc.protocol.java.data.world.Particle;
import org.cloudburstmc.protocol.java.data.world.ParticleType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class JavaPacketHelper {
    protected static final InternalLogger log = InternalLoggerFactory.getInstance(JavaPacketHelper.class);

    protected final Int2ObjectBiMap<EntityType> entityTypes = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<BlockEntityAction> blockEntityActions = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<ContainerType> containerTypes = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<EntityDataType<?>> entityDataTypes = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<Pose> poses = new Int2ObjectBiMap<>();

    protected JavaPacketHelper() {
        this.registerEntityTypes();
        this.registerBlockEntityActions();
    }

    public int readVarInt(ByteBuf buffer) {
        // don't use the signed version! Only Bedrock knows that concept
        return VarInts.readUnsignedInt(buffer);
    }

    public void writeVarInt(ByteBuf buffer, int varint) {
        // don't use the signed version! Only Bedrock knows that concept
        VarInts.writeUnsignedInt(buffer, varint);
    }

    public byte[] readByteArray(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        int length = VarInts.readUnsignedInt(buffer);
        Preconditions.checkArgument(buffer.isReadable(length),
                 "Tried to read %s bytes but only has %s readable", length, buffer.readableBytes());
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return bytes;
    }

    public void writeByteArray(ByteBuf buffer, byte[] bytes) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(bytes, "bytes");
        VarInts.writeUnsignedInt(buffer, bytes.length);
        buffer.writeBytes(bytes);
    }

    public String readString(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        return new String(readByteArray(buffer), StandardCharsets.UTF_8);
    }

    public void writeString(ByteBuf buffer, String string) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(string, "string");
        writeByteArray(buffer, string.getBytes(StandardCharsets.UTF_8));
    }

    public Key readKey(ByteBuf buffer) {
        return Key.key(this.readString(buffer));
    }

    public void writeKey(ByteBuf buffer, Key key) {
        this.writeString(buffer, key.asString());
    }

    public GameProfile readGameProfile(ByteBuf buffer) {
        UUID id = readUUID(buffer);
        String name = null;
        if (buffer.readableBytes() > 0) {
            name = readString(buffer);
        }
        GameProfile profile = new GameProfile(id, name);
        if (buffer.readableBytes() <= 0) {
            return profile;
        }
        // Read properties
        int propertiesAmt = VarInts.readUnsignedInt(buffer);
        PropertyMap properties = new PropertyMap();
        for (int i = 0; i < propertiesAmt; i++) {
            Property property = new Property(readString(buffer), readString(buffer), buffer.readBoolean() ? readString(buffer) : null);
            properties.put(property.getName(), property);
        }
        profile.setProperties(properties);
        return profile;
    }

    public void writeGameProfile(ByteBuf buffer, GameProfile profile) {
        writeGameProfile(buffer, profile, false);
    }

    public void writeGameProfile(ByteBuf buffer, GameProfile profile, boolean writeProperties) {
        writeUUID(buffer, profile.getId());
        writeString(buffer, profile.getName() == null ? "" : profile.getName());
        if (writeProperties && profile.getProperties() != null && !profile.getProperties().isEmpty()) {
            VarInts.writeUnsignedInt(buffer, profile.getProperties().size());
            for (Property property : profile.getProperties().values()) {
                writeString(buffer, property.getName());
                writeString(buffer, property.getValue());
                buffer.writeBoolean(property.getSignature() != null);
                if (property.getSignature() != null) {
                    writeString(buffer, property.getSignature());
                }
            }
        }
    }

    public UUID readUUID(ByteBuf buffer) {
        return new UUID(buffer.readLong(), buffer.readLong());
    }

    public void writeUUID(ByteBuf buffer, UUID uuid) {
        buffer.writeLong(uuid.getMostSignificantBits());
        buffer.writeLong(uuid.getLeastSignificantBits());
    }

    public Vector3d readPosition(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        return Vector3d.from(x, y, z);
    }

    public void writePosition(ByteBuf buffer, Vector3d vector3d) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector3d, "vector3d");
        buffer.writeDouble(vector3d.getX());
        buffer.writeDouble(vector3d.getY());
        buffer.writeDouble(vector3d.getZ());
    }

    public Vector3d readVelocity(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        double x = buffer.readShort() / 8000D;
        double y = buffer.readShort() / 8000D;
        double z = buffer.readShort() / 8000D;
        return Vector3d.from(x, y, z);
    }

    public void writeVelocity(ByteBuf buffer, Vector3d vector3d) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector3d, "vector3d");
        buffer.writeDouble(vector3d.getX() * 8000D);
        buffer.writeDouble(vector3d.getY() * 8000D);
        buffer.writeDouble(vector3d.getZ() * 8000D);
    }

    public Vector2f readRotation2f(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        float pitch = buffer.readByte() * 360 / 256F;
        float yaw = buffer.readByte() * 360 / 256F;
        return Vector2f.from(pitch, yaw);
    }

    public Vector3f readRotation3f(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        float pitch = buffer.readByte() * 360 / 256F;
        float yaw = buffer.readByte() * 360 / 256F;
        float headYaw = buffer.readByte() * 360 / 256F;
        return Vector3f.from(pitch, yaw, headYaw);
    }

    public void writeRotation2f(ByteBuf buffer, Vector2f vector2f) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector2f, "vector2f");
        buffer.writeByte((int) (vector2f.getX() * 256F / 360));
        buffer.writeByte((int) (vector2f.getY() * 256F / 360));
    }

    public void writeRotation3f(ByteBuf buffer, Vector3f vector3f) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector3f, "vector3f");
        buffer.writeByte((int) (vector3f.getX() * 256F / 360));
        buffer.writeByte((int) (vector3f.getY() * 256F / 360));
        buffer.writeByte((int) (vector3f.getZ() * 256F / 360));
    }

    public Vector3f readRotation(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        return Vector3f.from(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    public void writeRotation(ByteBuf buffer, Vector3f vector3f) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector3f, "vector3f");
        buffer.writeFloat(vector3f.getX());
        buffer.writeFloat(vector3f.getY());
        buffer.writeFloat(vector3f.getZ());
    }

    public Direction readDirection(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        return Direction.getById(this.readVarInt(buffer));
    }

    public void writeDirection(ByteBuf buffer, Direction direction) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(direction, "direction");
        this.writeVarInt(buffer, direction.ordinal());
    }

    public VillagerData readVillagerData(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        return new VillagerData(this.readVarInt(buffer), this.readVarInt(buffer), this.readVarInt(buffer));
    }

    public void writeVillagerData(ByteBuf buffer, VillagerData villagerData) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(villagerData, "villagerData");
        this.writeVarInt(buffer, villagerData.getLevel());
        this.writeVarInt(buffer, villagerData.getProfession());
        this.writeVarInt(buffer, villagerData.getType());
    }

    public Particle readParticle(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        return new Particle(ParticleType.getById(this.readVarInt(buffer)), null); // TODO
    }

    public void writeParticle(ByteBuf buffer, Particle particle) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(particle, "particle");
        this.writeVarInt(buffer, particle.getType().ordinal());
        // TODO: Write object data
    }

    // TODO: Move these to version helpers as they have changed between versions
    public ItemStack readItemStack(ByteBuf buffer) {
        boolean present = buffer.readBoolean();
        if (!present) {
            return null;
        }

        int item = VarInts.readUnsignedInt(buffer);
        return new ItemStack(item, buffer.readByte(), readTag(buffer));
    }

    public void writeItemStack(ByteBuf buffer, ItemStack item) {
        buffer.writeBoolean(item != null);
        if (item != null) {
            VarInts.writeUnsignedInt(buffer, item.getId());
            buffer.writeByte(item.getAmount());
            writeTag(buffer, item.getNbt());
        }
    }

    public Vector3i readBlockPosition(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        long position = buffer.readLong();
        return Vector3i.from((int) (position >> 38), (int) (position >> 12), (int) (position << 26 >> 38));
    }

    public void writeBlockPosition(ByteBuf buffer, Vector3i vector3i) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector3i, "vector3i");
        buffer.writeLong((long) (vector3i.getX() & 0x3FFFFFF) << 38 | (long) (vector3i.getZ() & 0x3FFFFFF) << 12 | (long) (vector3i.getY() & 0xFFF));
    }
    
    /*
        Helper array serialization
     */

    public <T> void readArray(ByteBuf buffer, Collection<T> array, BiFunction<ByteBuf, JavaPacketHelper, T> function) {
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer, this));
        }
    }

    public <T> void writeArray(ByteBuf buffer, Collection<T> array, TriConsumer<ByteBuf, JavaPacketHelper, T> biConsumer) {
        VarInts.writeUnsignedInt(buffer, array.size());
        for (T val : array) {
            biConsumer.accept(buffer, this, val);
        }
    }

    public <T> T[] readArray(ByteBuf buffer, T[] array, BiFunction<ByteBuf, JavaPacketHelper, T> function) {
        ObjectArrayList<T> list = new ObjectArrayList<>();
        readArray(buffer, list, function);
        return list.toArray(array);
    }

    public <T> void writeArray(ByteBuf buffer, T[] array, TriConsumer<ByteBuf, JavaPacketHelper, T> biConsumer) {
        VarInts.writeUnsignedInt(buffer, array.length);
        for (T val : array) {
            biConsumer.accept(buffer, this, val);
        }
    }

    public <T> void readArrayShortLE(ByteBuf buffer, Collection<T> array, BiFunction<ByteBuf, JavaPacketHelper, T> function) {
        int length = buffer.readUnsignedShortLE();
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer, this));
        }
    }

    public <T> void writeArrayShortLE(ByteBuf buffer, Collection<T> array, TriConsumer<ByteBuf, JavaPacketHelper, T> biConsumer) {
        buffer.writeShortLE(array.size());
        for (T val : array) {
            biConsumer.accept(buffer, this, val);
        }
    }

    /*
        Non-helper array serialization
     */

    public <T> void readArray(ByteBuf buffer, Collection<T> array, Function<ByteBuf, T> function) {
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer));
        }
    }

    public <T> void writeArray(ByteBuf buffer, Collection<T> array, BiConsumer<ByteBuf, T> biConsumer) {
        VarInts.writeUnsignedInt(buffer, array.size());
        for (T val : array) {
            biConsumer.accept(buffer, val);
        }
    }

    public <T> T[] readArray(ByteBuf buffer, T[] array, Function<ByteBuf, T> function) {
        ObjectArrayList<T> list = new ObjectArrayList<>();
        readArray(buffer, list, function);
        return list.toArray(array);
    }

    public <T> void writeArray(ByteBuf buffer, T[] array, BiConsumer<ByteBuf, T> biConsumer) {
        VarInts.writeUnsignedInt(buffer, array.length);
        for (T val : array) {
            biConsumer.accept(buffer, val);
        }
    }

    public <T> void readArrayShortLE(ByteBuf buffer, Collection<T> array, Function<ByteBuf, T> function) {
        int length = buffer.readUnsignedShortLE();
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer));
        }
    }

    public <T> void writeArrayShortLE(ByteBuf buffer, Collection<T> array, BiConsumer<ByteBuf, T> biConsumer) {
        buffer.writeShortLE(array.size());
        for (T val : array) {
            biConsumer.accept(buffer, val);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T readTag(ByteBuf buffer) {
        try (NBTInputStream reader = NbtUtils.createReader(new ByteBufInputStream(buffer))) {
            return (T) reader.readTag();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void writeTag(ByteBuf buffer, T tag) {
        try (NBTOutputStream writer = NbtUtils.createWriter(new ByteBufOutputStream(buffer))) {
            writer.writeTag(tag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Component readComponent(ByteBuf buffer) {
        return GsonComponentSerializer.gson().deserialize(this.readString(buffer));
    }

    public void writeComponent(ByteBuf buffer, Component component) {
        this.writeString(buffer, GsonComponentSerializer.gson().serialize(component));
    }

    public Pose readPose(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        return this.getPose(this.readVarInt(buffer));
    }

    public void writePose(ByteBuf buffer, Pose pose) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(pose, "pose");
        this.writeVarInt(buffer, this.getPoseId(pose));
    }

    public <T> EntityData<T> readEntityData(int dataId, int id, ByteBuf buffer) {
        EntityDataType<T> type = this.getEntityDataType(id);
        return new EntityData<>(dataId, type, type.read(this, buffer));
    }

    public <T> void writeEntityData(EntityData<T> data, ByteBuf buffer) {
        data.getType().write(this, buffer, data.getValue());
    }

    public <T> T readOptional(ByteBuf buffer, Function<ByteBuf, T> readFunction) {
        if (buffer.readBoolean()) {
            return readFunction.apply(buffer);
        }
        return null;
    }

    public <T> void writeOptional(ByteBuf buffer, T value, BiConsumer<ByteBuf, T> writeFunction) {
        buffer.writeBoolean(value != null);
        if (value != null) {
            writeFunction.accept(buffer, value);
        }
    }

    public final int getEntityId(EntityType entityType) {
        return this.entityTypes.get(entityType);
    }

    public final EntityType getEntityType(int entityId) {
        return this.entityTypes.get(entityId);
    }

    public final int getBlockEntityActionId(BlockEntityAction action) {
        return this.blockEntityActions.get(action);
    }

    public final BlockEntityAction getBlockEntityAction(int actionId) {
        return this.blockEntityActions.get(actionId);
    }

    public final int getContainerId(ContainerType containerType) {
        return this.containerTypes.get(containerType);
    }

    public final ContainerType getContainerType(int containerId) {
        return this.containerTypes.get(containerId);
    }

    public final Pose getPose(int id) {
        return this.poses.get(id);
    }

    public final int getPoseId(Pose pose) {
        return this.poses.get(pose);
    }

    @SuppressWarnings("unchecked")
    public final <T> EntityDataType<T> getEntityDataType(int id) {
        return (EntityDataType<T>) this.entityDataTypes.get(id);
    }

    public final int getEntityDataTypeId(EntityDataType<?> type) {
        return this.entityDataTypes.get(type);
    }

    protected abstract void registerEntityTypes();

    protected abstract void registerBlockEntityActions();

    protected abstract void registerContainerTypes();

    protected abstract void registerEntityDataTypes();

    public abstract void registerPoses();
}
