package com.nukkitx.protocol.bedrock.v291.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.network.util.Preconditions;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.data.event.*;
import com.nukkitx.protocol.bedrock.packet.EventPacket;
import com.nukkitx.protocol.util.TriConsumer;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.function.BiFunction;

import static com.nukkitx.protocol.bedrock.data.event.EventDataType.*;

public class EventSerializer_v291 implements BedrockPacketSerializer<EventPacket> {
    public static final EventSerializer_v291 INSTANCE = new EventSerializer_v291();

    protected static final EventDataType[] VALUES = EventDataType.values();

    protected final EnumMap<EventDataType, BiFunction<ByteBuf, BedrockPacketHelper, EventData>> readers = new EnumMap<>(EventDataType.class);
    protected final EnumMap<EventDataType, TriConsumer<ByteBuf, BedrockPacketHelper, EventData>> writers = new EnumMap<>(EventDataType.class);

    protected EventSerializer_v291() {
        this.readers.put(ACHIEVEMENT_AWARDED, this::readAchievementAwarded);
        this.readers.put(ENTITY_INTERACT, this::readEntityInteract);
        this.readers.put(PORTAL_BUILT, this::readPortalBuilt);
        this.readers.put(PORTAL_USED, this::readPortalUsed);
        this.readers.put(MOB_KILLED, this::readMobKilled);
        this.readers.put(CAULDRON_USED, this::readCauldronUsed);
        this.readers.put(PLAYER_DIED, this::readPlayerDied);
        this.readers.put(BOSS_KILLED, this::readBossKilled);
        this.readers.put(AGENT_COMMAND, this::readAgentCommand);
        this.readers.put(AGENT_CREATED, (b, h) -> AgentCreatedEventData.INSTANCE);
        this.readers.put(PATTERN_REMOVED, this::readPatternRemoved);
        this.readers.put(SLASH_COMMAND_EXECUTED, this::readSlashCommandExecuted);
        this.readers.put(FISH_BUCKETED, this::readFishBucketed);

        this.writers.put(ACHIEVEMENT_AWARDED, this::writeAchievementAwarded);
        this.writers.put(ENTITY_INTERACT, this::writeEntityInteract);
        this.writers.put(PORTAL_BUILT, this::writePortalBuilt);
        this.writers.put(PORTAL_USED, this::writePortalUsed);
        this.writers.put(MOB_KILLED, this::writeMobKilled);
        this.writers.put(CAULDRON_USED, this::writeCauldronUsed);
        this.writers.put(PLAYER_DIED, this::writePlayerDied);
        this.writers.put(BOSS_KILLED, this::writeBossKilled);
        this.writers.put(AGENT_COMMAND, this::writeAgentCommand);
        this.writers.put(AGENT_CREATED, (b, h, e) -> {
        });
        this.writers.put(PATTERN_REMOVED, this::writePatternRemoved);
        this.writers.put(SLASH_COMMAND_EXECUTED, this::writeSlashCommandExecuted);
        this.writers.put(FISH_BUCKETED, this::writeFishBucketed);
    }

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, EventPacket packet) {
        VarInts.writeLong(buffer, packet.getUniqueEntityId());
        EventData eventData = packet.getEventData();
        VarInts.writeInt(buffer, eventData.getType().ordinal());
        buffer.writeByte(packet.getUsePlayerId());

        TriConsumer<ByteBuf, BedrockPacketHelper, EventData> function = this.writers.get(eventData.getType());

        if (function == null) {
            throw new UnsupportedOperationException("Unknown event type " + eventData.getType());
        }

        function.accept(buffer, helper, eventData);
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, EventPacket packet) {
        packet.setUniqueEntityId(VarInts.readLong(buffer));

        int eventId = VarInts.readInt(buffer);
        Preconditions.checkElementIndex(eventId, VALUES.length, "EventDataType");
        EventDataType type = VALUES[eventId];

        packet.setUsePlayerId(buffer.readByte());

        BiFunction<ByteBuf, BedrockPacketHelper, EventData> function = this.readers.get(type);

        if (function == null) {
            throw new UnsupportedOperationException("Unknown event type " + type);
        }

        function.apply(buffer, helper);
    }

    protected AchievementAwardedEventData readAchievementAwarded(ByteBuf buffer, BedrockPacketHelper helper) {
        int achievementId = VarInts.readInt(buffer);
        return new AchievementAwardedEventData(achievementId);
    }

    protected void writeAchievementAwarded(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        AchievementAwardedEventData event = (AchievementAwardedEventData) eventData;
        VarInts.writeInt(buffer, event.getAchievementId());
    }

    protected EntityInteractEventData readEntityInteract(ByteBuf buffer, BedrockPacketHelper helper) {
        int interactionType = VarInts.readInt(buffer);
        int interactionEntityType = VarInts.readInt(buffer);
        int entityVariant = VarInts.readInt(buffer);
        int entityColor = buffer.readUnsignedByte();
        return new EntityInteractEventData(interactionType, interactionEntityType, entityVariant, entityColor);
    }

    protected void writeEntityInteract(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        EntityInteractEventData event = (EntityInteractEventData) eventData;
        VarInts.writeInt(buffer, event.getInteractionType());
        VarInts.writeInt(buffer, event.getLegacyEntityTypeId());
        VarInts.writeInt(buffer, event.getVariant());
        buffer.writeByte(event.getPaletteColor());
    }

    protected PortalBuiltEventData readPortalBuilt(ByteBuf buffer, BedrockPacketHelper helper) {
        int dimensionId = VarInts.readInt(buffer);
        return new PortalBuiltEventData(dimensionId);
    }

    protected void writePortalBuilt(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        PortalBuiltEventData event = (PortalBuiltEventData) eventData;
        VarInts.writeInt(buffer, event.getDimensionId());
    }

    protected PortalUsedEventData readPortalUsed(ByteBuf buffer, BedrockPacketHelper helper) {
        int fromDimensionId = VarInts.readInt(buffer);
        int toDimensionId = VarInts.readInt(buffer);
        return new PortalUsedEventData(fromDimensionId, toDimensionId);
    }

    protected void writePortalUsed(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        PortalUsedEventData event = (PortalUsedEventData) eventData;
        VarInts.writeInt(buffer, event.getFromDimensionId());
        VarInts.writeInt(buffer, event.getToDimensionId());
    }

    protected MobKilledEventData readMobKilled(ByteBuf buffer, BedrockPacketHelper helper) {
        long killerUniqueEntityId = VarInts.readLong(buffer);
        long victimUniqueEntityId = VarInts.readLong(buffer);
        int entityDamageCause = VarInts.readInt(buffer);
        int villagerTradeTier = VarInts.readInt(buffer);
        String villagerDisplayName = helper.readString(buffer);
        return new MobKilledEventData(killerUniqueEntityId, victimUniqueEntityId, -1, entityDamageCause,
                villagerTradeTier, villagerDisplayName);
    }

    protected void writeMobKilled(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        MobKilledEventData event = (MobKilledEventData) eventData;
        VarInts.writeLong(buffer, event.getKillerUniqueEntityId());
        VarInts.writeLong(buffer, event.getVictimUniqueEntityId());
        VarInts.writeInt(buffer, event.getEntityDamageCause());
        VarInts.writeInt(buffer, event.getVillagerTradeTier());
        helper.writeString(buffer, event.getVillagerDisplayName());
    }

    protected CauldronUsedEventData readCauldronUsed(ByteBuf buffer, BedrockPacketHelper helper) {
        int potionId = VarInts.readInt(buffer);
        int color = VarInts.readInt(buffer);
        int fillLevel = VarInts.readInt(buffer);
        return new CauldronUsedEventData(potionId, color, fillLevel);
    }

    protected void writeCauldronUsed(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        CauldronUsedEventData event = (CauldronUsedEventData) eventData;
        VarInts.writeUnsignedInt(buffer, event.getPotionId());
        VarInts.writeInt(buffer, event.getColor());
        VarInts.writeInt(buffer, event.getFillLevel());
    }

    protected PlayerDiedEventData readPlayerDied(ByteBuf buffer, BedrockPacketHelper helper) {
        int attackerEntityId = VarInts.readInt(buffer);
        int entityDamageCause = VarInts.readInt(buffer);
        return new PlayerDiedEventData(attackerEntityId, -1, entityDamageCause, false);
    }

    protected void writePlayerDied(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        PlayerDiedEventData event = (PlayerDiedEventData) eventData;
        VarInts.writeInt(buffer, event.getAttackerEntityId());
        VarInts.writeInt(buffer, event.getEntityDamageCause());
    }

    protected BossKilledEventData readBossKilled(ByteBuf buffer, BedrockPacketHelper helper) {
        long bossUniqueEntityId = VarInts.readLong(buffer);
        int playerPartySize = VarInts.readInt(buffer);
        int interactionEntityType = VarInts.readInt(buffer);
        return new BossKilledEventData(bossUniqueEntityId, playerPartySize, interactionEntityType);
    }

    protected void writeBossKilled(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        BossKilledEventData event = (BossKilledEventData) eventData;
        VarInts.writeLong(buffer, event.getBossUniqueEntityId());
        VarInts.writeInt(buffer, event.getPlayerPartySize());
        VarInts.writeInt(buffer, event.getBossEntityType());
    }

    protected AgentCommandEventData readAgentCommand(ByteBuf buffer, BedrockPacketHelper helper) {
        int agentResult = VarInts.readInt(buffer);
        int dataValue = VarInts.readInt(buffer);
        String command = helper.readString(buffer);
        String dataKey = helper.readString(buffer);
        String output = helper.readString(buffer);
        return new AgentCommandEventData(agentResult, command, dataKey, dataValue, output);
    }

    protected void writeAgentCommand(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        AgentCommandEventData event = (AgentCommandEventData) eventData;
        VarInts.writeInt(buffer, event.getAgentResult());
        VarInts.writeInt(buffer, event.getDataValue());
        helper.writeString(buffer, event.getCommand());
        helper.writeString(buffer, event.getDataKey());
        helper.writeString(buffer, event.getOutput());
    }

    protected PatternRemovedEventData readPatternRemoved(ByteBuf buffer, BedrockPacketHelper helper) {
        int itemId = VarInts.readInt(buffer);
        int auxValue = VarInts.readInt(buffer);
        int patternsSize = VarInts.readInt(buffer);
        int patternIndex = VarInts.readInt(buffer);
        int patternColor = VarInts.readInt(buffer);
        return new PatternRemovedEventData(itemId, auxValue, patternsSize, patternIndex, patternColor);
    }

    protected void writePatternRemoved(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        PatternRemovedEventData event = (PatternRemovedEventData) eventData;
        VarInts.writeInt(buffer, event.getItemId());
        VarInts.writeInt(buffer, event.getAuxValue());
        VarInts.writeInt(buffer, event.getPatternsSize());
        VarInts.writeInt(buffer, event.getPatternIndex());
        VarInts.writeInt(buffer, event.getPatternColor());
    }

    protected SlashCommandExecutedEventData readSlashCommandExecuted(ByteBuf buffer, BedrockPacketHelper helper) {
        int successCount = VarInts.readInt(buffer);
        VarInts.readInt(buffer);
        String commandName = helper.readString(buffer);
        List<String> outputMessages = Arrays.asList(helper.readString(buffer).split(";"));
        return new SlashCommandExecutedEventData(commandName, successCount, outputMessages);
    }

    protected void writeSlashCommandExecuted(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        SlashCommandExecutedEventData event = (SlashCommandExecutedEventData) eventData;
        VarInts.writeInt(buffer, event.getSuccessCount());
        List<String> outputMessages = event.getOutputMessages();
        VarInts.writeInt(buffer, outputMessages.size());
        helper.writeString(buffer, event.getCommandName());
        helper.writeString(buffer, String.join(";", outputMessages));
    }

    protected FishBucketedEventData readFishBucketed(ByteBuf buffer, BedrockPacketHelper helper) {
        int pattern = VarInts.readInt(buffer);
        int preset = VarInts.readInt(buffer);
        int bucketedEntityType = VarInts.readInt(buffer);
        boolean isRelease = buffer.readBoolean();
        return new FishBucketedEventData(pattern, preset, bucketedEntityType, isRelease);
    }

    protected void writeFishBucketed(ByteBuf buffer, BedrockPacketHelper helper, EventData eventData) {
        FishBucketedEventData event = (FishBucketedEventData) eventData;
        VarInts.writeInt(buffer, event.getPattern());
        VarInts.writeInt(buffer, event.getPreset());
        VarInts.writeInt(buffer, event.getBucketedEntityType());
        buffer.writeBoolean(event.isReleaseEvent());
    }
}
