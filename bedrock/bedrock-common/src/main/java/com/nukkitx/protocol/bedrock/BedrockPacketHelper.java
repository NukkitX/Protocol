package com.nukkitx.protocol.bedrock;

import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.math.vector.Vector4f;
import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NBTOutputStream;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.network.VarInts;
import com.nukkitx.network.util.Preconditions;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.data.command.*;
import com.nukkitx.protocol.bedrock.data.entity.*;
import com.nukkitx.protocol.bedrock.data.inventory.*;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionType;
import com.nukkitx.protocol.bedrock.data.skin.AnimationData;
import com.nukkitx.protocol.bedrock.data.skin.ImageData;
import com.nukkitx.protocol.bedrock.data.skin.SerializedSkin;
import com.nukkitx.protocol.bedrock.data.structure.StructureSettings;
import com.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket;
import com.nukkitx.protocol.bedrock.util.TriConsumer;
import com.nukkitx.protocol.util.Int2ObjectBiMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.util.AsciiString;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public abstract class BedrockPacketHelper {
    protected static final InternalLogger log = InternalLoggerFactory.getInstance(BedrockPacketHelper.class);

    protected final Int2ObjectBiMap<EntityData> entityData = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<EntityFlag> entityFlags = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<EntityData.Type> entityDataTypes = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<EntityEventType> entityEvents = new Int2ObjectBiMap<>();
    protected final Object2IntMap<Class<?>> gameRuleTypes = new Object2IntOpenHashMap<>(3, 0.5f);
    protected final Int2ObjectBiMap<SoundEvent> soundEvents = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<LevelEventType> levelEvents = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<CommandParam> commandParams = new Int2ObjectBiMap<>();
    protected final Int2ObjectBiMap<ResourcePackType> resourcePackTypes = new Int2ObjectBiMap<>();

    protected BedrockPacketHelper() {
        gameRuleTypes.defaultReturnValue(-1);

        this.registerEntityDataTypes();
        this.registerEntityData();
        this.registerEntityFlags();
        this.registerEntityEvents();
        this.registerGameRuleTypes();
        this.registerSoundEvents();
        this.registerLevelEvents();
        this.registerCommandParams();
        this.registerResourcePackTypes();
    }

    protected final void addGameRuleType(int index, Class<?> clazz) {
        this.gameRuleTypes.put(clazz, index);
    }

    protected final void addEntityData(int index, EntityData entityData) {
        this.entityData.put(index, entityData);
    }

    protected final void addEntityFlag(int index, EntityFlag flag) {
        this.entityFlags.put(index, flag);
    }

    protected final void addEntityDataType(int index, EntityData.Type type) {
        this.entityDataTypes.put(index, type);
    }

    protected final void addEntityEvent(int index, EntityEventType type) {
        this.entityEvents.put(index, type);
    }

    protected final void addSoundEvent(int index, SoundEvent soundEvent) {
        this.soundEvents.put(index, soundEvent);
    }

    protected final void addLevelEvent(int index, LevelEventType levelEventType) {
        this.levelEvents.put(index, levelEventType);
    }

    public final int getEntityEventId(EntityEventType type) {
        // @TODO For speed we may want a flag that disables this check for production use
        if (!this.entityEvents.containsValue(type)) {
            log.debug("Unknown EntityEventType: {}", type);
            return this.entityEvents.get(EntityEventType.NONE);
        }
        return this.entityEvents.get(type);
    }

    public final EntityEventType getEntityEvent(int id) {
        // @TODO For speed we may want a flag that disables this check for production use
        if (!entityEvents.containsKey(id)) {
            log.debug("Unknown EntityEvent: {}", id);
            return EntityEventType.NONE;
        }
        return this.entityEvents.get(id);
    }

    public final int getSoundEventId(SoundEvent event) {
        if (!soundEvents.containsValue(event)) {
            log.debug("Unknown SoundEvent {} received", event);
            return soundEvents.get(SoundEvent.UNDEFINED);
        }
        return this.soundEvents.get(event);
    }

    public final SoundEvent getSoundEvent(int id) {
        SoundEvent soundEvent = this.soundEvents.get(id);
        if (soundEvent == null) {
            log.debug("Unknown SoundEvent {} received", Integer.toUnsignedLong(id));
            return SoundEvent.UNDEFINED;
        }
        return soundEvent;
    }

    public final int getLevelEventId(LevelEventType event) {
        // @TODO For speed we may want a flag that disables this check for production use
        if (!this.levelEvents.containsValue(event)) {
            log.debug("Unknown LevelEventType: {}", event);
            return this.levelEvents.get(LevelEventType.UNDEFINED);
        }
        return this.levelEvents.get(event);
    }

    public final LevelEventType getLevelEvent(int id) {
        LevelEventType levelEvent = this.levelEvents.get(id);
        if (levelEvent == null) {
            log.debug("Unknown LevelEventType {} received", id);
            return LevelEventType.UNDEFINED;
        }
        return levelEvent;

    }

    public final void addCommandParam(int index, CommandParam commandParam) {
        this.commandParams.put(index, commandParam);
    }

    public final CommandParam getCommandParam(int index) {
        CommandParam commandParam = this.commandParams.get(index);
        if (commandParam == null) {
            log.debug("Requested undefined CommandParam {}", index);
            return new CommandParam(index);
        }
        return commandParam;
    }

    public final int getCommandParamId(CommandParam commandParam) {
        return this.commandParams.get(commandParam);
    }

    public final void removeCommandParam(int index) {
        this.commandParams.remove(index);
    }

    public final void removeCommandParam(CommandParam type) {
        this.commandParams.remove(type);
    }
    
    public final void addResourcePackType(int index, ResourcePackType resourcePackType) {
        this.resourcePackTypes.put(index, resourcePackType);
    }

    public final ResourcePackType getResourcePackType(int index) {
        return this.resourcePackTypes.get(index);
    }

    public final int getResourcePackTypeId(ResourcePackType resourcePackType) {
        return this.resourcePackTypes.get(resourcePackType);
    }

    protected abstract void registerEntityData();

    protected abstract void registerEntityFlags();

    protected abstract void registerEntityDataTypes();

    protected abstract void registerEntityEvents();

    protected abstract void registerGameRuleTypes();

    protected abstract void registerSoundEvents();

    protected abstract void registerCommandParams();

    protected abstract void registerResourcePackTypes();

    protected abstract void registerLevelEvents();

    public abstract EntityLinkData readEntityLink(ByteBuf buffer);

    public abstract void writeEntityLink(ByteBuf buffer, EntityLinkData link);

    public abstract ItemData readNetItem(ByteBuf buffer, BedrockSession session);

    public abstract void writeNetItem(ByteBuf buffer, ItemData item, BedrockSession session);

    public abstract ItemData readItem(ByteBuf buffer, BedrockSession session);

    public abstract void writeItem(ByteBuf buffer, ItemData item, BedrockSession session);

    public abstract CommandOriginData readCommandOrigin(ByteBuf buffer);

    public abstract void writeCommandOrigin(ByteBuf buffer, CommandOriginData commandOrigin);

    public abstract GameRuleData<?> readGameRule(ByteBuf buffer);

    public abstract void writeGameRule(ByteBuf buffer, GameRuleData<?> gameRule);

    public abstract void readEntityData(ByteBuf buffer, EntityDataMap entityData);

    public abstract void writeEntityData(ByteBuf buffer, EntityDataMap entityData);

    public abstract CommandEnumData readCommandEnum(ByteBuf buffer, boolean soft);

    public abstract void writeCommandEnum(ByteBuf buffer, CommandEnumData commandEnum);

    public abstract StructureSettings readStructureSettings(ByteBuf buffer);

    public abstract void writeStructureSettings(ByteBuf buffer, StructureSettings settings);

    public abstract SerializedSkin readSkin(ByteBuf buffer);

    public abstract void writeSkin(ByteBuf buffer, SerializedSkin skin);

    public abstract AnimationData readAnimationData(ByteBuf buffer);

    public abstract void writeAnimationData(ByteBuf buffer, AnimationData animation);

    public abstract ImageData readImage(ByteBuf buffer);

    public abstract void writeImage(ByteBuf buffer, ImageData image);

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

    public ByteBuf readBuffer(ByteBuf buffer) {
        int length = VarInts.readUnsignedInt(buffer);
        return buffer.readRetainedSlice(length);
    }

    public void writeBuffer(ByteBuf buffer, ByteBuf toWrite) {
        Preconditions.checkNotNull(toWrite, "toWrite");
        VarInts.writeUnsignedInt(buffer, toWrite.readableBytes());
        buffer.writeBytes(toWrite, toWrite.readerIndex(), toWrite.writerIndex());
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

    public AsciiString readLEAsciiString(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");

        int length = buffer.readIntLE();
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new AsciiString(bytes);
    }

    public void writeLEAsciiString(ByteBuf buffer, AsciiString string) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(string, "string");
        buffer.writeIntLE(string.length());
        buffer.writeBytes(string.toByteArray());
    }

    public UUID readUuid(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        return new UUID(buffer.readLongLE(), buffer.readLongLE());
    }

    public void writeUuid(ByteBuf buffer, UUID uuid) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(uuid, "uuid");
        buffer.writeLongLE(uuid.getMostSignificantBits());
        buffer.writeLongLE(uuid.getLeastSignificantBits());
    }

    public Vector4f readVector4f(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        float w = buffer.readFloatLE();
        float x = buffer.readFloatLE();
        float y = buffer.readFloatLE();
        float z = buffer.readFloatLE();
        return Vector4f.from(x, y, z, w);
    }

    public void writeVector4f(ByteBuf buffer, Vector4f vector4f) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector4f, "vector4f");
        buffer.writeFloatLE(vector4f.getW());
        buffer.writeFloatLE(vector4f.getX());
        buffer.writeFloatLE(vector4f.getY());
        buffer.writeFloatLE(vector4f.getZ());
    }

    public Vector3f readVector3f(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        float x = buffer.readFloatLE();
        float y = buffer.readFloatLE();
        float z = buffer.readFloatLE();
        return Vector3f.from(x, y, z);
    }

    public void writeVector3f(ByteBuf buffer, Vector3f vector3f) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector3f, "vector3f");
        buffer.writeFloatLE(vector3f.getX());
        buffer.writeFloatLE(vector3f.getY());
        buffer.writeFloatLE(vector3f.getZ());
    }

    public Vector2f readVector2f(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        float x = buffer.readFloatLE();
        float y = buffer.readFloatLE();
        return Vector2f.from(x, y);
    }

    public void writeVector2f(ByteBuf buffer, Vector2f vector2f) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector2f, "vector2f");
        buffer.writeFloatLE(vector2f.getX());
        buffer.writeFloatLE(vector2f.getY());
    }


    public Vector3i readVector3i(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        int x = VarInts.readInt(buffer);
        int y = VarInts.readInt(buffer);
        int z = VarInts.readInt(buffer);

        return Vector3i.from(x, y, z);
    }

    public void writeVector3i(ByteBuf buffer, Vector3i vector3i) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(vector3i, "vector3i");
        VarInts.writeInt(buffer, vector3i.getX());
        VarInts.writeInt(buffer, vector3i.getY());
        VarInts.writeInt(buffer, vector3i.getZ());
    }

    public Vector3i readBlockPosition(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        int x = VarInts.readInt(buffer);
        int y = VarInts.readUnsignedInt(buffer);
        int z = VarInts.readInt(buffer);

        return Vector3i.from(x, y, z);
    }

    public void writeBlockPosition(ByteBuf buffer, Vector3i blockPosition) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(blockPosition, "blockPosition");
        VarInts.writeInt(buffer, blockPosition.getX());
        VarInts.writeUnsignedInt(buffer, blockPosition.getY());
        VarInts.writeInt(buffer, blockPosition.getZ());
    }

    public Vector3i readOrigBlockPosition(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        int x = VarInts.readInt(buffer);
        int y = VarInts.readUnsignedInt(buffer);
        int z = VarInts.readInt(buffer);

        return Vector3i.from(x, y, z);
    }

    public void writeOrigBlockPosition(ByteBuf buffer, Vector3i blockPosition) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(blockPosition, "blockPosition");
        VarInts.writeInt(buffer, blockPosition.getX());
        VarInts.writeUnsignedInt(buffer, blockPosition.getY());
        VarInts.writeInt(buffer, blockPosition.getZ());
    }

    public Vector3f readByteRotation(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        float pitch = readByteAngle(buffer);
        float yaw = readByteAngle(buffer);
        float roll = readByteAngle(buffer);
        return Vector3f.from(pitch, yaw, roll);
    }

    public void writeByteRotation(ByteBuf buffer, Vector3f rotation) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(rotation, "rotation");
        writeByteAngle(buffer, rotation.getX());
        writeByteAngle(buffer, rotation.getY());
        writeByteAngle(buffer, rotation.getZ());
    }

    public float readByteAngle(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");
        return buffer.readByte() * (360f / 256f);
    }

    public void writeByteAngle(ByteBuf buffer, float angle) {
        Preconditions.checkNotNull(buffer, "buffer");
        buffer.writeByte((byte) (angle / (360f / 256f)));
    }

    /*
        Helper array serialization
     */

    public <T> void readArray(ByteBuf buffer, Collection<T> array, BiFunction<ByteBuf, BedrockPacketHelper, T> function) {
        int length = VarInts.readUnsignedInt(buffer);
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer, this));
        }
    }

    public <T> void writeArray(ByteBuf buffer, Collection<T> array, TriConsumer<ByteBuf, BedrockPacketHelper, T> biConsumer) {
        VarInts.writeUnsignedInt(buffer, array.size());
        for (T val : array) {
            biConsumer.accept(buffer, this, val);
        }
    }

    public <T> T[] readArray(ByteBuf buffer, T[] array, BiFunction<ByteBuf, BedrockPacketHelper, T> function) {
        ObjectArrayList<T> list = new ObjectArrayList<>();
        readArray(buffer, list, function);
        return list.toArray(array);
    }

    public <T> void writeArray(ByteBuf buffer, T[] array, TriConsumer<ByteBuf, BedrockPacketHelper, T> biConsumer) {
        VarInts.writeUnsignedInt(buffer, array.length);
        for (T val : array) {
            biConsumer.accept(buffer, this, val);
        }
    }

    public <T> void readArrayShortLE(ByteBuf buffer, Collection<T> array, BiFunction<ByteBuf, BedrockPacketHelper, T> function) {
        int length = buffer.readUnsignedShortLE();
        for (int i = 0; i < length; i++) {
            array.add(function.apply(buffer, this));
        }
    }

    public <T> void writeArrayShortLE(ByteBuf buffer, Collection<T> array, TriConsumer<ByteBuf, BedrockPacketHelper, T> biConsumer) {
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
        try (NBTInputStream reader = NbtUtils.createNetworkReader(new ByteBufInputStream(buffer))) {
            return (T) reader.readTag();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void writeTag(ByteBuf buffer, T tag) {
        try (NBTOutputStream writer = NbtUtils.createNetworkWriter(new ByteBufOutputStream(buffer))) {
            writer.writeTag(tag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readItemUse(ByteBuf buffer, InventoryTransactionPacket packet, BedrockSession session) {
        packet.setActionType(VarInts.readUnsignedInt(buffer));
        packet.setBlockPosition(this.readBlockPosition(buffer));
        packet.setBlockFace(VarInts.readInt(buffer));
        packet.setHotbarSlot(VarInts.readInt(buffer));
        packet.setItemInHand(this.readItem(buffer, session));
        packet.setPlayerPosition(this.readVector3f(buffer));
        packet.setClickPosition(this.readVector3f(buffer));
    }

    public void writeItemUse(ByteBuf buffer, InventoryTransactionPacket packet, BedrockSession session) {
        VarInts.writeUnsignedInt(buffer, packet.getActionType());
        this.writeBlockPosition(buffer, packet.getBlockPosition());
        VarInts.writeInt(buffer, packet.getBlockFace());
        VarInts.writeInt(buffer, packet.getHotbarSlot());
        this.writeItem(buffer, packet.getItemInHand(), session);
        this.writeVector3f(buffer, packet.getPlayerPosition());
        this.writeVector3f(buffer, packet.getClickPosition());
    }

    public InventoryActionData readInventoryAction(ByteBuf buffer, BedrockSession session, boolean hasNetworkIds) {
        InventorySource source = this.readSource(buffer);
        int slot = VarInts.readUnsignedInt(buffer);
        ItemData fromItem = this.readItem(buffer, session);
        ItemData toItem = this.readItem(buffer, session);

        return new InventoryActionData(source, slot, fromItem, toItem);
    }

    public void writeInventoryAction(ByteBuf buffer, InventoryActionData action, BedrockSession session, boolean hasNetworkIds) {
        requireNonNull(action, "InventoryActionData was null");

        this.writeSource(buffer, action.getSource());
        VarInts.writeUnsignedInt(buffer, action.getSlot());
        this.writeItem(buffer, action.getFromItem(), session);
        this.writeItem(buffer, action.getToItem(), session);
    }

    public InventorySource readSource(ByteBuf buffer) {
        InventorySource.Type type = InventorySource.Type.byId(VarInts.readUnsignedInt(buffer));

        switch (type) {
            case CONTAINER:
                int containerId = VarInts.readInt(buffer);
                return InventorySource.fromContainerWindowId(containerId);
            case GLOBAL:
                return InventorySource.fromGlobalInventory();
            case WORLD_INTERACTION:
                InventorySource.Flag flag = InventorySource.Flag.values()[VarInts.readUnsignedInt(buffer)];
                return InventorySource.fromWorldInteraction(flag);
            case CREATIVE:
                return InventorySource.fromCreativeInventory();
            case NON_IMPLEMENTED_TODO:
                containerId = VarInts.readInt(buffer);
                return InventorySource.fromNonImplementedTodo(containerId);
            default:
                return InventorySource.fromInvalid();
        }
    }

    public void writeSource(ByteBuf buffer, InventorySource inventorySource) {
        requireNonNull(inventorySource, "InventorySource was null");

        VarInts.writeUnsignedInt(buffer, inventorySource.getType().id());

        switch (inventorySource.getType()) {
            case CONTAINER:
            case UNTRACKED_INTERACTION_UI:
            case NON_IMPLEMENTED_TODO:
                VarInts.writeInt(buffer, inventorySource.getContainerId());
                break;
            case WORLD_INTERACTION:
                VarInts.writeUnsignedInt(buffer, inventorySource.getFlag().ordinal());
                break;
        }
    }

    public ItemData readRecipeIngredient(ByteBuf buffer) {
        requireNonNull(buffer, "buffer is null");

        int id = VarInts.readInt(buffer);

        if (id == 0) {
            // We don't need to read anything extra.
            return ItemData.AIR;
        }

        int meta = VarInts.readInt(buffer);
        int count = VarInts.readInt(buffer);

        return ItemData.of(id, (short) meta, count);
    }

    public void writeRecipeIngredient(ByteBuf buffer, ItemData item) {
        requireNonNull(buffer, "buffer is null");
        requireNonNull(item, "item is null");

        VarInts.writeInt(buffer, item.getId());

        if (item.getId() == 0) {
            return;
        }

        VarInts.writeInt(buffer, item.getDamage());
        VarInts.writeInt(buffer, item.getCount());
    }

    public PotionMixData readPotionRecipe(ByteBuf buffer) {
        requireNonNull(buffer, "buffer is null");

        return new PotionMixData(
                VarInts.readInt(buffer),
                VarInts.readInt(buffer),
                VarInts.readInt(buffer),
                VarInts.readInt(buffer),
                VarInts.readInt(buffer),
                VarInts.readInt(buffer)
        );
    }

    public void writePotionRecipe(ByteBuf buffer, PotionMixData data) {
        requireNonNull(buffer, "buffer is null");
        requireNonNull(data, "data is null");

        VarInts.writeInt(buffer, data.getInputId());
        VarInts.writeInt(buffer, data.getInputMeta());
        VarInts.writeInt(buffer, data.getReagentId());
        VarInts.writeInt(buffer, data.getReagentMeta());
        VarInts.writeInt(buffer, data.getOutputId());
        VarInts.writeInt(buffer, data.getOutputMeta());
    }

    public ContainerMixData readContainerChangeRecipe(ByteBuf buffer) {
        requireNonNull(buffer, "buffer is null");

        return new ContainerMixData(
                VarInts.readInt(buffer),
                VarInts.readInt(buffer),
                VarInts.readInt(buffer)
        );
    }

    public void writeContainerChangeRecipe(ByteBuf buffer, ContainerMixData data) {
        requireNonNull(buffer, "buffer is null");
        requireNonNull(data, "data is null");

        VarInts.writeInt(buffer, data.getInputId());
        VarInts.writeInt(buffer, data.getReagentId());
        VarInts.writeInt(buffer, data.getOutputId());
    }

    public CommandEnumConstraintData readCommandEnumConstraints(ByteBuf buffer, List<CommandEnumData> enums, List<String> enumValues) {
        int valueIndex = buffer.readIntLE();
        int enumIndex = buffer.readIntLE();
        CommandEnumConstraintType[] constraints = readArray(buffer, new CommandEnumConstraintType[0],
                buf -> CommandEnumConstraintType.byId(buffer.readByte()));

        return new CommandEnumConstraintData(
                enumValues.get(valueIndex),
                enums.get(enumIndex),
                constraints
        );
    }

    public void writeCommandEnumConstraints(ByteBuf buffer, CommandEnumConstraintData data, List<CommandEnumData> enums, List<String> enumValues) {
        buffer.writeIntLE(enumValues.indexOf(data.getOption()));
        buffer.writeIntLE(enums.indexOf(data.getEnumData()));
        writeArray(buffer, data.getConstraints(), (buf, constraint) -> {
            buf.writeByte(constraint.ordinal());
        });
    }

    /**
     * Return true if the item id has a blockingticks attached.
     *
     * Only a shield should return true
     *
     * @param id ID of item
     * @param lookFor ID to match
     * @return true if reading/writing blockingticks
     */
    public boolean isBlockingItem(int id, int lookFor) {
        return id == lookFor;
    }

    public void readExperiments(ByteBuf buffer, List<ExperimentData> experiments) {
        throw new UnsupportedOperationException();
    }

    public void writeExperiments(ByteBuf buffer, List<ExperimentData> experiments) {
        throw new UnsupportedOperationException();
    }

    protected void registerStackActionRequestTypes() {
        throw new UnsupportedOperationException();
    }

    public StackRequestActionType getStackRequestActionTypeFromId(int id) {
        throw new UnsupportedOperationException();
    }

    public int getIdFromStackRequestActionType(StackRequestActionType type) {
        throw new UnsupportedOperationException();
    }

    public ItemStackRequest readItemStackRequest(ByteBuf buffer, BedrockSession session) {
        throw new UnsupportedOperationException();
    }

    public void writeItemStackRequest(ByteBuf buffer, BedrockSession session, ItemStackRequest request) {
        throw new UnsupportedOperationException();
    }
}
