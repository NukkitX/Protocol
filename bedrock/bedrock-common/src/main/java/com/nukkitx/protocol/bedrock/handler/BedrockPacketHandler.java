package com.nukkitx.protocol.bedrock.handler;

import com.nukkitx.protocol.PacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import com.nukkitx.protocol.genoa.packet.*;

public interface BedrockPacketHandler extends PacketHandler {
    //Minecraft Earth (Genoa)
    default boolean handle(GenoaOpenInventoryPacket packet){return false;}

    default boolean handle(GenoaInventoryDataPacket packet){return false;}

    default boolean handle(GenoaGuestPlayerJoinResponsePacket packet){return false;}

    default boolean handle(GenoaShareAnchorPacket packet){return false;}

    default boolean handle(GenoaItemPickupPacket packet){return false;}

    default boolean handle(GenoaSetActorMolangVariablesPacket packet){return false;}

    default boolean handle(PersonaMobRequestPacket packet){return false;}

    default boolean handle(GenoaGuestPlayerJoinRequestPacket packet){return false;}

    default boolean handle(GenoaNetworkTransformPacket packet){return false;}

    default boolean handle(GenoaWorldManipulationPacket packet){return false;}

    default boolean handle(GenoaNetworkOwnershipRequestPacket packet){return false;}

    default boolean handle(GenoaItemParticlePacket packet) { return false; };

    default boolean handle(GenoaThirdPersonItemParticlePacket packet) { return false; };

    default boolean handle(GenoaNetworkOwnershipStatusPacket packet) { return false; };

    default boolean handle(GenoaUpdateBlockPacket packet) { return false; };

    default boolean handle(GenoaBlockDestroyPacket packet) { return false; };

    default boolean handle(GenoaPlayerHurtPacket packet) { return false; };

    default boolean handle(GenoaDisconnectRequest packet) { return false; };

    default boolean handle(GenoaDisconnectStart packet) { return false; };

    default boolean handle(GenoaGameplaySettings packet) { return false; };

    default boolean handle(GenoaBlockHitNoDamagePacket packet) { return false; };

    default boolean handle(GenoaItemBrokeNotificationPacket packet) { return false; };

    default boolean handle(GenoaItemAwardedNotificationPacket packet) { return false; };

    default boolean handle(GenoaEventFlatbufferPacket packet) { return false; };

    //Default bedrock
    default boolean handle(AdventureSettingsPacket packet) {
        return false;
    }

    default boolean handle(AnimatePacket packet) {
        return false;
    }

    default boolean handle(AnvilDamagePacket packet) {
        return false;
    }

    default boolean handle(AvailableEntityIdentifiersPacket packet) {
        return false;
    }

    default boolean handle(BlockEntityDataPacket packet) {
        return false;
    }

    default boolean handle(BlockPickRequestPacket packet) {
        return false;
    }

    default boolean handle(BookEditPacket packet) {
        return false;
    }

    default boolean handle(ClientCacheBlobStatusPacket packet) {
        return false;
    }

    default boolean handle(ClientCacheMissResponsePacket packet) {
        return false;
    }

    default boolean handle(ClientCacheStatusPacket packet) {
        return false;
    }

    default boolean handle(ClientToServerHandshakePacket packet) {
        return false;
    }

    default boolean handle(CommandBlockUpdatePacket packet) {
        return false;
    }

    default boolean handle(CommandRequestPacket packet) {
        return false;
    }

    default boolean handle(CompletedUsingItemPacket packet) {
        return false;
    }

    default boolean handle(ContainerClosePacket packet) {
        return false;
    }

    default boolean handle(CraftingEventPacket packet) {
        return false;
    }

    default boolean handle(EducationSettingsPacket packet) {
        return false;
    }

    default boolean handle(EmotePacket packet) {
        return false;
    }

    default boolean handle(EntityEventPacket packet) {
        return false;
    }

    default boolean handle(EntityFallPacket packet) {
        return false;
    }

    default boolean handle(EntityPickRequestPacket packet) {
        return false;
    }

    default boolean handle(EventPacket packet) {
        return false;
    }

    default boolean handle(FilterTextPacket packet) {
        return false;
    }

    default boolean handle(InteractPacket packet) {
        return false;
    }

    default boolean handle(InventoryContentPacket packet) {
        return false;
    }

    default boolean handle(InventorySlotPacket packet) {
        return false;
    }

    default boolean handle(InventoryTransactionPacket packet) {
        return false;
    }

    default boolean handle(ItemFrameDropItemPacket packet) {
        return false;
    }

    default boolean handle(LabTablePacket packet) {
        return false;
    }

    default boolean handle(LecternUpdatePacket packet) {
        return false;
    }

    default boolean handle(LevelEventGenericPacket packet) {
        return false;
    }

    default boolean handle(LevelSoundEvent1Packet packet) {
        return false;
    }

    default boolean handle(LevelSoundEventPacket packet) {
        return false;
    }

    default boolean handle(LoginPacket packet) {
        return false;
    }

    default boolean handle(MapInfoRequestPacket packet) {
        return false;
    }

    default boolean handle(MobArmorEquipmentPacket packet) {
        return false;
    }

    default boolean handle(MobEquipmentPacket packet) {
        return false;
    }

    default boolean handle(ModalFormResponsePacket packet) {
        return false;
    }

    default boolean handle(MoveEntityAbsolutePacket packet) {
        return false;
    }

    default boolean handle(MovePlayerPacket packet) {
        return false;
    }

    default boolean handle(MultiplayerSettingsPacket packet) {
        return false;
    }

    default boolean handle(NetworkStackLatencyPacket packet) {
        return false;
    }

    default boolean handle(PhotoTransferPacket packet) {
        return false;
    }

    default boolean handle(PlayerActionPacket packet) {
        return false;
    }

    default boolean handle(PlayerAuthInputPacket packet) {
        return false;
    }

    default boolean handle(PlayerHotbarPacket packet) {
        return false;
    }

    default boolean handle(PlayerInputPacket packet) {
        return false;
    }

    default boolean handle(PlayerSkinPacket packet) {
        return false;
    }

    default boolean handle(PurchaseReceiptPacket packet) {
        return false;
    }

    default boolean handle(RequestChunkRadiusPacket packet) {
        return false;
    }

    default boolean handle(ResourcePackChunkRequestPacket packet) {
        return false;
    }

    default boolean handle(ResourcePackClientResponsePacket packet) {
        return false;
    }

    default boolean handle(RiderJumpPacket packet) {
        return false;
    }

    default boolean handle(ServerSettingsRequestPacket packet) {
        return false;
    }

    default boolean handle(SetDefaultGameTypePacket packet) {
        return false;
    }

    default boolean handle(SetLocalPlayerAsInitializedPacket packet) {
        return false;
    }

    default boolean handle(SetPlayerGameTypePacket packet) {
        return false;
    }

    default boolean handle(SubClientLoginPacket packet) {
        return false;
    }

    default boolean handle(AddBehaviorTreePacket packet) {
        return false;
    }

    default boolean handle(AddEntityPacket packet) {
        return false;
    }

    default boolean handle(AddHangingEntityPacket packet) {
        return false;
    }

    default boolean handle(AddItemEntityPacket packet) {
        return false;
    }

    default boolean handle(AddPaintingPacket packet) {
        return false;
    }

    default boolean handle(AddPlayerPacket packet) {
        return false;
    }

    default boolean handle(AvailableCommandsPacket packet) {
        return false;
    }

    default boolean handle(BlockEventPacket packet) {
        return false;
    }

    default boolean handle(BossEventPacket packet) {
        return false;
    }

    default boolean handle(CameraPacket packet) {
        return false;
    }

    default boolean handle(ChangeDimensionPacket packet) {
        return false;
    }

    default boolean handle(ChunkRadiusUpdatedPacket packet) {
        return false;
    }

    default boolean handle(ClientboundMapItemDataPacket packet) {
        return false;
    }

    default boolean handle(CommandOutputPacket packet) {
        return false;
    }

    default boolean handle(ContainerOpenPacket packet) {
        return false;
    }

    default boolean handle(ContainerSetDataPacket packet) {
        return false;
    }

    default boolean handle(CraftingDataPacket packet) {
        return false;
    }

    default boolean handle(DisconnectPacket packet) {
        return false;
    }

    default boolean handle(ExplodePacket packet) {
        return false;
    }

    default boolean handle(LevelChunkPacket packet) {
        return false;
    }

    default boolean handle(GameRulesChangedPacket packet) {
        return false;
    }

    default boolean handle(GuiDataPickItemPacket packet) {
        return false;
    }

    default boolean handle(HurtArmorPacket packet) {
        return false;
    }

    default boolean handle(AutomationClientConnectPacket packet) {
        return false;
    }

    default boolean handle(LevelEventPacket packet) {
        return false;
    }

    default boolean handle(MapCreateLockedCopyPacket packet) {
        return false;
    }

    default boolean handle(MobEffectPacket packet) {
        return false;
    }

    default boolean handle(ModalFormRequestPacket packet) {
        return false;
    }

    default boolean handle(MoveEntityDeltaPacket packet) {
        return false;
    }

    default boolean handle(NetworkSettingsPacket packet) {
        return false;
    }

    default boolean handle(NpcRequestPacket packet) {
        return false;
    }

    default boolean handle(OnScreenTextureAnimationPacket packet) {
        return false;
    }

    default boolean handle(PlayerListPacket packet) {
        return false;
    }

    default boolean handle(PlaySoundPacket packet) {
        return false;
    }

    default boolean handle(PlayStatusPacket packet) {
        return false;
    }

    default boolean handle(RemoveEntityPacket packet) {
        return false;
    }

    default boolean handle(RemoveObjectivePacket packet) {
        return false;
    }

    default boolean handle(ResourcePackChunkDataPacket packet) {
        return false;
    }

    default boolean handle(ResourcePackDataInfoPacket packet) {
        return false;
    }

    default boolean handle(ResourcePacksInfoPacket packet) {
        return false;
    }

    default boolean handle(ResourcePackStackPacket packet) {
        return false;
    }

    default boolean handle(RespawnPacket packet) {
        return false;
    }

    default boolean handle(ScriptCustomEventPacket packet) {
        return false;
    }

    default boolean handle(ServerSettingsResponsePacket packet) {
        return false;
    }

    default boolean handle(ServerToClientHandshakePacket packet) {
        return false;
    }

    default boolean handle(SetCommandsEnabledPacket packet) {
        return false;
    }

    default boolean handle(SetDifficultyPacket packet) {
        return false;
    }

    default boolean handle(SetDisplayObjectivePacket packet) {
        return false;
    }

    default boolean handle(SetEntityDataPacket packet) {
        return false;
    }

    default boolean handle(SetEntityLinkPacket packet) {
        return false;
    }

    default boolean handle(SetEntityMotionPacket packet) {
        return false;
    }

    default boolean handle(SetHealthPacket packet) {
        return false;
    }

    default boolean handle(SetLastHurtByPacket packet) {
        return false;
    }

    default boolean handle(SetScoreboardIdentityPacket packet) {
        return false;
    }

    default boolean handle(SetScorePacket packet) {
        return false;
    }

    default boolean handle(SetSpawnPositionPacket packet) {
        return false;
    }

    default boolean handle(SetTimePacket packet) {
        return false;
    }

    default boolean handle(SettingsCommandPacket packet) {
        return false;
    }

    default boolean handle(SetTitlePacket packet) {
        return false;
    }

    default boolean handle(ShowCreditsPacket packet) {
        return false;
    }

    default boolean handle(ShowProfilePacket packet) {
        return false;
    }

    default boolean handle(ShowStoreOfferPacket packet) {
        return false;
    }

    default boolean handle(SimpleEventPacket packet) {
        return false;
    }

    default boolean handle(SpawnExperienceOrbPacket packet) {
        return false;
    }

    default boolean handle(SpawnParticleEffectPacket packet) {
        return false;
    }

    default boolean handle(StartGamePacket packet) {
        return false;
    }

    default boolean handle(StopSoundPacket packet) {
        return false;
    }

    default boolean handle(StructureBlockUpdatePacket packet) {
        return false;
    }

    default boolean handle(StructureTemplateDataRequestPacket packet) {
        return false;
    }

    default boolean handle(StructureTemplateDataResponsePacket packet) {
        return false;
    }

    default boolean handle(TakeItemEntityPacket packet) {
        return false;
    }

    default boolean handle(TextPacket packet) {
        return false;
    }

    default boolean handle(TickSyncPacket packet) {
        return false;
    }

    default boolean handle(TransferPacket packet) {
        return false;
    }

    default boolean handle(UpdateAttributesPacket packet) {
        return false;
    }

    default boolean handle(UpdateBlockPacket packet) {
        return false;
    }

    default boolean handle(UpdateBlockPropertiesPacket packet) {
        return false;
    }

    default boolean handle(UpdateBlockSyncedPacket packet) {
        return false;
    }

    default boolean handle(UpdateEquipPacket packet) {
        return false;
    }

    default boolean handle(UpdateSoftEnumPacket packet) {
        return false;
    }

    default boolean handle(UpdateTradePacket packet) {
        return false;
    }

    default boolean handle(BiomeDefinitionListPacket packet) {
        return false;
    }

    default boolean handle(LevelSoundEvent2Packet packet) {
        return false;
    }

    default boolean handle(NetworkChunkPublisherUpdatePacket packet) {
        return false;
    }

    default boolean handle(VideoStreamConnectPacket packet) {
        return false;
    }

    default boolean handle(CodeBuilderPacket packet) {
        return false;
    }

    default boolean handle(EmoteListPacket packet) {
        return false;
    }

    default boolean handle(ItemStackRequestPacket packet) {
        return false;
    }

    default boolean handle(ItemStackResponsePacket packet) {
        return false;
    }

    default boolean handle(PlayerArmorDamagePacket packet) {
        return false;
    }

    default boolean handle(PlayerEnchantOptionsPacket packet) {
        return false;
    }

    default boolean handle(CreativeContentPacket packet) {
        return false;
    }

    default boolean handle(UpdatePlayerGameTypePacket packet) {
        return false;
    }

    default boolean handle(PositionTrackingDBServerBroadcastPacket packet) {
        return false;
    }

    default boolean handle(PositionTrackingDBClientRequestPacket packet) {
        return false;
    }

    default boolean handle(PacketViolationWarningPacket packet) {
        return false;
    }

    default boolean handle(DebugInfoPacket packet) {
        return false;
    }

    default boolean handle(MotionPredictionHintsPacket packet) {
        return false;
    }

    default boolean handle(AnimateEntityPacket packet) {
        return false;
    }

    default boolean handle(CameraShakePacket packet) {
        return false;
    }

    default boolean handle(CorrectPlayerMovePredictionPacket packet) {
        return false;
    }

    default boolean handle(PlayerFogPacket packet) {
        return false;
    }

    default boolean handle(ItemComponentPacket packet) {
        return false;
    }

    default boolean handle(ClientboundDebugRendererPacket packet) { return false; }

}
