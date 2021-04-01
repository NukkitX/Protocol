package com.nukkitx.protocol.genoa.v425;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NBTInputStream;
import com.nukkitx.nbt.NBTOutputStream;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtUtils;
import com.nukkitx.network.VarInts;
import com.nukkitx.network.util.Preconditions;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.data.LevelEventType;
import com.nukkitx.protocol.bedrock.data.ResourcePackType;
import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import com.nukkitx.protocol.bedrock.data.entity.EntityDataMap;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlag;
import com.nukkitx.protocol.bedrock.data.entity.EntityFlags;
import com.nukkitx.protocol.bedrock.data.inventory.ItemData;
import com.nukkitx.protocol.bedrock.data.skin.AnimatedTextureType;
import com.nukkitx.protocol.bedrock.data.skin.AnimationData;
import com.nukkitx.protocol.bedrock.data.skin.ImageData;
import com.nukkitx.protocol.bedrock.data.skin.SerializedSkin;
import com.nukkitx.protocol.bedrock.data.structure.StructureMirror;
import com.nukkitx.protocol.bedrock.data.structure.StructureRotation;
import com.nukkitx.protocol.bedrock.data.structure.StructureSettings;
import com.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket;
import com.nukkitx.protocol.bedrock.util.LittleEndianByteBufOutputStream;
import com.nukkitx.protocol.bedrock.v354.BedrockPacketHelper_v354;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaPacketHelper extends BedrockPacketHelper_v354 {

    public static final GenoaPacketHelper INSTANCE = new GenoaPacketHelper();

    protected static final AnimatedTextureType[] TEXTURE_TYPES = AnimatedTextureType.values();

    @Override
    protected void registerEntityData() {
        super.registerEntityData();

        this.addEntityData(40, EntityData.NPC_DATA);

        this.addEntityData(82,EntityData.GENOA_ENTITY_DATA); // TODO: Find out what this is

        this.addEntityData(103, EntityData.SKIN_ID);
        this.addEntityData(104, EntityData.SPAWNING_FRAMES);
        this.addEntityData(105, EntityData.COMMAND_BLOCK_TICK_DELAY);
        this.addEntityData(106, EntityData.COMMAND_BLOCK_EXECUTE_ON_FIRST_TICK);
    }

    @Override
    protected void registerEntityFlags() {
        super.registerEntityFlags();

        this.addEntityFlag(87, EntityFlag.HIDDEN_WHEN_INVISIBLE);
    }

    @Override
    protected void registerLevelEvents() {
        super.registerLevelEvents();

        this.addLevelEvent(23 + 2000, LevelEventType.PARTICLE_TELEPORT_TRAIL);

        int legacy = 0x4000;
        this.addLevelEvent(1 + legacy, LevelEventType.PARTICLE_BUBBLE);
        this.addLevelEvent(2 + legacy, LevelEventType.PARTICLE_BUBBLE_MANUAL);
        this.addLevelEvent(3 + legacy, LevelEventType.PARTICLE_CRITICAL);
        this.addLevelEvent(4 + legacy, LevelEventType.PARTICLE_BLOCK_FORCE_FIELD);
        this.addLevelEvent(5 + legacy, LevelEventType.PARTICLE_SMOKE);
        this.addLevelEvent(6 + legacy, LevelEventType.PARTICLE_EXPLODE);
        this.addLevelEvent(7 + legacy, LevelEventType.PARTICLE_EVAPORATION);
        this.addLevelEvent(8 + legacy, LevelEventType.PARTICLE_FLAME);
        this.addLevelEvent(9 + legacy, LevelEventType.PARTICLE_LAVA);
        this.addLevelEvent(10 + legacy, LevelEventType.PARTICLE_LARGE_SMOKE);
        this.addLevelEvent(11 + legacy, LevelEventType.PARTICLE_REDSTONE);
        this.addLevelEvent(12 + legacy, LevelEventType.PARTICLE_RISING_RED_DUST);
        this.addLevelEvent(13 + legacy, LevelEventType.PARTICLE_ITEM_BREAK);
        this.addLevelEvent(14 + legacy, LevelEventType.PARTICLE_SNOWBALL_POOF);
        this.addLevelEvent(15 + legacy, LevelEventType.PARTICLE_HUGE_EXPLODE);
        this.addLevelEvent(16 + legacy, LevelEventType.PARTICLE_HUGE_EXPLODE_SEED);
        this.addLevelEvent(17 + legacy, LevelEventType.PARTICLE_MOB_FLAME);
        this.addLevelEvent(18 + legacy, LevelEventType.PARTICLE_HEART);
        this.addLevelEvent(19 + legacy, LevelEventType.PARTICLE_TERRAIN);
        this.addLevelEvent(20 + legacy, LevelEventType.PARTICLE_TOWN_AURA);
        this.addLevelEvent(21 + legacy, LevelEventType.PARTICLE_PORTAL);
        this.addLevelEvent(22 + legacy, LevelEventType.PARTICLE_MOB_PORTAL);
        this.addLevelEvent(23 + legacy, LevelEventType.PARTICLE_SPLASH);
        this.addLevelEvent(24 + legacy, LevelEventType.PARTICLE_SPLASH_MANUAL);
        this.addLevelEvent(25 + legacy, LevelEventType.PARTICLE_WATER_WAKE);
        this.addLevelEvent(26 + legacy, LevelEventType.PARTICLE_DRIP_WATER);
        this.addLevelEvent(27 + legacy, LevelEventType.PARTICLE_DRIP_LAVA);
        this.addLevelEvent(28 + legacy, LevelEventType.PARTICLE_FALLING_DUST);
        this.addLevelEvent(29 + legacy, LevelEventType.PARTICLE_MOB_SPELL);
        this.addLevelEvent(30 + legacy, LevelEventType.PARTICLE_MOB_SPELL_AMBIENT);
        this.addLevelEvent(31 + legacy, LevelEventType.PARTICLE_MOB_SPELL_INSTANTANEOUS);
        this.addLevelEvent(32 + legacy, LevelEventType.PARTICLE_INK);
        this.addLevelEvent(33 + legacy, LevelEventType.PARTICLE_SLIME);
        this.addLevelEvent(34 + legacy, LevelEventType.PARTICLE_RAIN_SPLASH);
        this.addLevelEvent(35 + legacy, LevelEventType.PARTICLE_VILLAGER_ANGRY);
        this.addLevelEvent(36 + legacy, LevelEventType.PARTICLE_VILLAGER_HAPPY);
        this.addLevelEvent(37 + legacy, LevelEventType.PARTICLE_ENCHANTMENT_TABLE);
        this.addLevelEvent(38 + legacy, LevelEventType.PARTICLE_TRACKING_EMITTER);
        this.addLevelEvent(39 + legacy, LevelEventType.PARTICLE_NOTE);
        this.addLevelEvent(40 + legacy, LevelEventType.PARTICLE_WITCH_SPELL);
        this.addLevelEvent(41 + legacy, LevelEventType.PARTICLE_CARROT);
        this.addLevelEvent(42 + legacy, LevelEventType.PARTICLE_MOB_APPEARANCE);
        this.addLevelEvent(43 + legacy, LevelEventType.PARTICLE_END_ROD);
        this.addLevelEvent(44 + legacy, LevelEventType.PARTICLE_DRAGONS_BREATH);
        this.addLevelEvent(45 + legacy, LevelEventType.PARTICLE_SPIT);
        this.addLevelEvent(46 + legacy, LevelEventType.PARTICLE_TOTEM);
        this.addLevelEvent(47 + legacy, LevelEventType.PARTICLE_FOOD);
        this.addLevelEvent(48 + legacy, LevelEventType.PARTICLE_FIREWORKS_STARTER);
        this.addLevelEvent(49 + legacy, LevelEventType.PARTICLE_FIREWORKS_SPARK);
        this.addLevelEvent(50 + legacy, LevelEventType.PARTICLE_FIREWORKS_OVERLAY);
        this.addLevelEvent(51 + legacy, LevelEventType.PARTICLE_BALLOON_GAS);
        this.addLevelEvent(52 + legacy, LevelEventType.PARTICLE_COLORED_FLAME);
        this.addLevelEvent(53 + legacy, LevelEventType.PARTICLE_SPARKLER);
        this.addLevelEvent(54 + legacy, LevelEventType.PARTICLE_CONDUIT);
        this.addLevelEvent(55 + legacy, LevelEventType.PARTICLE_BUBBLE_COLUMN_UP);
        this.addLevelEvent(56 + legacy, LevelEventType.PARTICLE_BUBBLE_COLUMN_DOWN);
        this.addLevelEvent(57 + legacy, LevelEventType.PARTICLE_SNEEZE);
    }

    @Override
    protected void registerResourcePackTypes() {
        addResourcePackType(0, ResourcePackType.INVALID);
        addResourcePackType(1, ResourcePackType.RESOURCE);
        addResourcePackType(2, ResourcePackType.BEHAVIOR);
        addResourcePackType(3, ResourcePackType.WORLD_TEMPLATE);
        addResourcePackType(4, ResourcePackType.ADDON);
        addResourcePackType(5, ResourcePackType.SKINS);
        addResourcePackType(6, ResourcePackType.CACHED);
        addResourcePackType(7, ResourcePackType.COPY_PROTECTED);
    }

    @Override
    public SerializedSkin readSkin(ByteBuf buffer) {
        String skinId = this.readString(buffer);
        String skinResourcePatch = this.readString(buffer);
        ImageData skinData = this.readImage(buffer);

        int animationCount = buffer.readIntLE();
        List<AnimationData> animations = new ObjectArrayList<>(animationCount);
        for (int i = 0; i < animationCount; i++) {
            animations.add(this.readAnimationData(buffer));
        }

        ImageData capeData = this.readImage(buffer);
        String geometryData = this.readString(buffer);
        String animationData = this.readString(buffer);
        boolean premium = buffer.readBoolean();
        boolean persona = buffer.readBoolean();
        boolean capeOnClassic = buffer.readBoolean();
        String capeId = this.readString(buffer);
        String fullSkinId = this.readString(buffer);

        return SerializedSkin.of(skinId, "", skinResourcePatch, skinData, animations, capeData, geometryData, animationData,
                premium, persona, capeOnClassic, capeId, fullSkinId);
    }

    @Override
    public void writeSkin(ByteBuf buffer, SerializedSkin skin) {
        requireNonNull(skin, "Skin is null");

        this.writeString(buffer, skin.getSkinId());
        this.writeString(buffer, skin.getSkinResourcePatch());
        this.writeImage(buffer, skin.getSkinData());

        List<AnimationData> animations = skin.getAnimations();
        buffer.writeIntLE(animations.size());
        for (AnimationData animation : animations) {
            this.writeAnimationData(buffer, animation);
        }

        this.writeImage(buffer, skin.getCapeData());
        this.writeString(buffer, skin.getGeometryData());
        this.writeString(buffer, skin.getAnimationData());
        buffer.writeBoolean(skin.isPremium());
        buffer.writeBoolean(skin.isPersona());
        buffer.writeBoolean(skin.isCapeOnClassic());
        this.writeString(buffer, skin.getCapeId());
        this.writeString(buffer, skin.getFullSkinId());
    }

    @Override
    public AnimationData readAnimationData(ByteBuf buffer) {
        ImageData image = this.readImage(buffer);
        AnimatedTextureType type = TEXTURE_TYPES[buffer.readIntLE()];
        float frames = buffer.readFloatLE();
        return new AnimationData(image, type, frames);
    }

    @Override
    public void writeAnimationData(ByteBuf buffer, AnimationData animation) {
        this.writeImage(buffer, animation.getImage());
        buffer.writeIntLE(animation.getTextureType().ordinal());
        buffer.writeFloatLE(animation.getFrames());
    }

    @Override
    public ImageData readImage(ByteBuf buffer) {
        requireNonNull(buffer, "buffer is null");
        int width = buffer.readIntLE();
        int height = buffer.readIntLE();
        byte[] image = readByteArray(buffer);
        return ImageData.of(width, height, image);
    }

    @Override
    public void writeImage(ByteBuf buffer, ImageData image) {
        requireNonNull(buffer, "buffer is null");
        requireNonNull(image, "image is null");

        buffer.writeIntLE(image.getWidth());
        buffer.writeIntLE(image.getHeight());
        writeByteArray(buffer, image.getImage());
    }

    @Override
    public void readEntityData(ByteBuf buffer, EntityDataMap entityDataMap) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(entityDataMap, "entityDataDictionary");

        int length = VarInts.readUnsignedInt(buffer);

        for (int i = 0; i < length; i++) {
            int metadataInt = VarInts.readUnsignedInt(buffer);
            EntityData entityData = this.entityData.get(metadataInt);
            EntityData.Type type = this.entityDataTypes.get(VarInts.readUnsignedInt(buffer));
            if (entityData != null && entityData.isFlags()) {
                if (type != EntityData.Type.LONG) {
                    throw new IllegalArgumentException("Expected long value for flags, got " + type.name());
                }
                type = EntityData.Type.FLAGS;
            }

            Object object;
            switch (type) {
                case BYTE:
                    object = buffer.readByte();
                    break;
                case SHORT:
                    object = buffer.readShortLE();
                    break;
                case INT:
                    object = VarInts.readInt(buffer);
                    break;
                case FLOAT:
                    object = buffer.readFloatLE();
                    break;
                case STRING:
                    object = readString(buffer);
                    break;
                case NBT:
                    object = this.readTag(buffer);
                    break;
                case VECTOR3I:
                    object = readVector3i(buffer);
                    break;
                case FLAGS:
                    int index = entityData == EntityData.FLAGS_2 ? 1 : 0;
                    entityDataMap.getOrCreateFlags().set(VarInts.readLong(buffer), index, this.entityFlags);
                    continue;
                case LONG:
                    object = VarInts.readLong(buffer);
                    break;
                case VECTOR3F:
                    object = readVector3f(buffer);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown entity data type received");
            }
            if (entityData != null) {
                entityDataMap.put(entityData, object);
            } else {
                log.debug("Unknown entity data: {} type {} value {}", metadataInt, type, object);
            }
        }
    }

    @Override
    public void writeEntityData(ByteBuf buffer, EntityDataMap entityDataMap) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(entityDataMap, "entityDataDictionary");

        VarInts.writeUnsignedInt(buffer, entityDataMap.size());

        for (Map.Entry<EntityData, Object> entry : entityDataMap.entrySet()) {
            int index = buffer.writerIndex();
            VarInts.writeUnsignedInt(buffer, this.entityData.get(entry.getKey()));
            Object object = entry.getValue();
            EntityData.Type type = EntityData.Type.from(object);
            VarInts.writeUnsignedInt(buffer, this.entityDataTypes.get(type));

            switch (type) {
                case BYTE:
                    buffer.writeByte((byte) object);
                    break;
                case SHORT:
                    buffer.writeShortLE((short) object);
                    break;
                case INT:
                    VarInts.writeInt(buffer, (int) object);
                    break;
                case FLOAT:
                    buffer.writeFloatLE((float) object);
                    break;
                case STRING:
                    writeString(buffer, (String) object);
                    break;
                case NBT:
                    NbtMap tag;
                    if (object instanceof NbtMap) {
                        tag = (NbtMap) object;
                    } else {
                        ItemData item = (ItemData) object;
                        tag = item.getTag();
                        if (tag == null) {
                            tag = NbtMap.EMPTY;
                        }
                    }
                    this.writeTag(buffer, tag);
                    break;
                case VECTOR3I:
                    writeVector3i(buffer, (Vector3i) object);
                    break;
                case FLAGS:
                    int flagsIndex = entry.getKey() == EntityData.FLAGS_2 ? 1 : 0;
                    object = ((EntityFlags) object).get(flagsIndex, this.entityFlags);
                case LONG:
                    VarInts.writeLong(buffer, (long) object);
                    break;
                case VECTOR3F:
                    writeVector3f(buffer, (Vector3f) object);
                    break;
                default:
                    buffer.writerIndex(index);
                    break;
            }
        }
    }

    @Override
    public void readItemUse(ByteBuf buffer, InventoryTransactionPacket packet, BedrockSession session) {

        packet.setActionType(VarInts.readUnsignedInt(buffer));
        packet.setBlockPosition(this.readGenoaBlockPosition(buffer));
        packet.setBlockFace(VarInts.readInt(buffer));
        packet.setHotbarSlot(VarInts.readInt(buffer));
        packet.setItemInHand(this.readItem(buffer, session));
        packet.setPlayerPosition(this.readVector3f(buffer));
        packet.setClickPosition(this.readVector3f(buffer));

        packet.setBlockRuntimeId(VarInts.readUnsignedInt(buffer));
    }

    @Override
    public void writeItemUse(ByteBuf buffer, InventoryTransactionPacket packet, BedrockSession session) {

        VarInts.writeUnsignedInt(buffer, packet.getActionType());
        this.writeGenoaBlockPosition(buffer, packet.getBlockPosition());
        VarInts.writeInt(buffer, packet.getBlockFace());
        VarInts.writeInt(buffer, packet.getHotbarSlot());
        this.writeItem(buffer, packet.getItemInHand(), session);
        this.writeVector3f(buffer, packet.getPlayerPosition());
        this.writeVector3f(buffer, packet.getClickPosition());

        VarInts.writeUnsignedInt(buffer, packet.getBlockRuntimeId());
    }

    public Vector3i readGenoaBlockPosition(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");

        int w = VarInts.readInt(buffer);
        int x = VarInts.readInt(buffer);
        int y = VarInts.readUnsignedInt(buffer);
        int z = VarInts.readInt(buffer);

        // Prev: Z X Y

        return Vector3i.from(x, y, z);
    }

    public void writeGenoaBlockPosition(ByteBuf buffer, Vector3i blockPosition) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(blockPosition, "blockPosition");

        VarInts.writeInt(buffer, -63);
        VarInts.writeInt(buffer, blockPosition.getX());
        VarInts.writeUnsignedInt(buffer, blockPosition.getY());
        VarInts.writeInt(buffer, blockPosition.getZ());
    }

    @Override
    public Vector3i readBlockPosition(ByteBuf buffer) {
        Preconditions.checkNotNull(buffer, "buffer");

        int x = VarInts.readInt(buffer);
        int y = VarInts.readUnsignedInt(buffer);
        int z = VarInts.readInt(buffer);

        return Vector3i.from(x, y, z);
    }

    @Override
    public void writeBlockPosition(ByteBuf buffer, Vector3i blockPosition) {
        Preconditions.checkNotNull(buffer, "buffer");
        Preconditions.checkNotNull(blockPosition, "blockPosition");
        VarInts.writeInt(buffer, blockPosition.getX());
        VarInts.writeUnsignedInt(buffer, blockPosition.getY());
        VarInts.writeInt(buffer, blockPosition.getZ());
    }

    @Override
    public StructureSettings readStructureSettings(ByteBuf buffer) {
        String paletteName = this.readString(buffer);
        boolean ignoringEntities = buffer.readBoolean();
        boolean ignoringBlocks = buffer.readBoolean();
        Vector3i size = this.readBlockPosition(buffer);
        Vector3i offset = this.readBlockPosition(buffer);
        long lastEditedByEntityId = VarInts.readLong(buffer);
        StructureRotation rotation = StructureRotation.from(buffer.readByte());
        StructureMirror mirror = StructureMirror.from(buffer.readByte());
        float integrityValue = buffer.readFloatLE();
        int integritySeed = buffer.readIntLE();

        return new StructureSettings(paletteName, ignoringEntities, ignoringBlocks, size, offset, lastEditedByEntityId,
                rotation, mirror, integrityValue, integritySeed, Vector3f.ZERO);
    }

    @Override
    public void writeStructureSettings(ByteBuf buffer, StructureSettings settings) {
        this.writeString(buffer, settings.getPaletteName());
        buffer.writeBoolean(settings.isIgnoringBlocks());
        buffer.writeBoolean(settings.isIgnoringBlocks());
        this.writeBlockPosition(buffer, settings.getSize());
        this.writeBlockPosition(buffer, settings.getOffset());
        VarInts.writeLong(buffer, settings.getLastEditedByEntityId());
        buffer.writeByte(settings.getRotation().ordinal());
        buffer.writeByte(settings.getMirror().ordinal());
        buffer.writeFloatLE(settings.getIntegrityValue());
        buffer.writeIntLE(settings.getIntegritySeed());
    }
}
