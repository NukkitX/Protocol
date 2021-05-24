package org.cloudburstmc.protocol.java.v754;

import lombok.experimental.UtilityClass;
import org.cloudburstmc.protocol.java.JavaPacketCodec;
import org.cloudburstmc.protocol.java.packet.State;
import org.cloudburstmc.protocol.java.packet.handshake.*;
import org.cloudburstmc.protocol.java.packet.login.*;
import org.cloudburstmc.protocol.java.packet.play.CustomPayloadPacket;
import org.cloudburstmc.protocol.java.packet.play.KeepAlivePacket;
import org.cloudburstmc.protocol.java.packet.play.SetCarriedItemPacket;
import org.cloudburstmc.protocol.java.packet.play.clientbound.*;
import org.cloudburstmc.protocol.java.packet.play.ContainerClosePacket;
import org.cloudburstmc.protocol.java.packet.play.serverbound.*;
import org.cloudburstmc.protocol.java.packet.status.*;
import org.cloudburstmc.protocol.java.v754.serializer.handshake.*;
import org.cloudburstmc.protocol.java.v754.serializer.login.*;
import org.cloudburstmc.protocol.java.v754.serializer.play.clientbound.*;
import org.cloudburstmc.protocol.java.v754.serializer.play.*;
import org.cloudburstmc.protocol.java.v754.serializer.play.serverbound.*;
import org.cloudburstmc.protocol.java.v754.serializer.status.*;

@UtilityClass
public class Java_v754 {
    public static final JavaPacketCodec V754_CODEC = JavaPacketCodec.builder()
            .protocolVersion(754)
            .minecraftVersion("1.16.4")
            .helper(JavaPacketHelper_v754.INSTANCE)
            .codec(State.HANDSHAKING, JavaPacketCodec.JavaStateCodec.builder()
                    .registerServerbound(HandshakingPacket.class, HandshakingSerializer_v754.INSTANCE, 0)
                    // .registerClientbound(LegacyServerListPingPacket.class, LegacyServerListPingSerializer_v754.INSTANCE, 254) // TODO
                    .build())
            .codec(State.STATUS, JavaPacketCodec.JavaStateCodec.builder()
                    .registerClientbound(StatusResponsePacket.class, StatusResponseSerializer_v754.INSTANCE, 0)
                    .registerClientbound(PongPacket.class, PongSerializer_v754.INSTANCE, 1)
                    .registerServerbound(StatusRequestPacket.class, StatusRequestSerializer_v754.INSTANCE, 0)
                    .registerServerbound(PingPacket.class, PingSerializer_v754.INSTANCE, 1)
                    .build())
            .codec(State.LOGIN, JavaPacketCodec.JavaStateCodec.builder()
                    .registerClientbound(org.cloudburstmc.protocol.java.packet.login.DisconnectPacket.class, org.cloudburstmc.protocol.java.v754.serializer.login.DisconnectSerializer_v754.INSTANCE, 0)
                    .registerClientbound(EncryptionRequestPacket.class, EncryptionRequestSerializer_v754.INSTANCE, 1)
                    .registerClientbound(LoginSuccessPacket.class, LoginSuccessSerializer_v754.INSTANCE, 2)
                    .registerClientbound(SetCompressionPacket.class, SetCompressionSerializer_v754.INSTANCE, 3)
                    .registerClientbound(CustomQueryRequestPacket.class, CustomQueryRequestSerializer_v754.INSTANCE, 4)
                    .registerServerbound(LoginStartPacket.class, LoginStartSerializer_v754.INSTANCE, 0)
                    .registerServerbound(EncryptionResponsePacket.class, EncryptionResponseSerializer_v754.INSTANCE, 1)
                    .registerServerbound(CustomQueryResponsePacket.class, CustomQueryResponseSerializer_v754.INSTANCE, 2)
                    .build())
            .codec(State.PLAY, JavaPacketCodec.JavaStateCodec.builder()
                    .registerClientbound(AddEntityPacket.class, AddEntitySerializer_v754.INSTANCE, 0)
                    .registerClientbound(AddExperienceOrbPacket.class, AddExperienceOrbSerializer_v754.INSTANCE, 1)
                    .registerClientbound(AddMobPacket.class, AddMobSerializer_v754.INSTANCE, 2)
                    .registerClientbound(AddPaintingPacket.class, AddPaintingSerializer_v754.INSTANCE, 3)
                    .registerClientbound(AddPlayerPacket.class, AddPlayerSerializer_v754.INSTANCE, 4)
                    .registerClientbound(AnimatePacket.class, AnimateSerializer_v754.INSTANCE, 5)
                    .registerClientbound(AwardStatsPacket.class, AwardStatsSerializer_v754.INSTANCE, 6)
                    .registerClientbound(BlockBreakAckPacket.class, BlockBreakAckSerializer_v754.INSTANCE, 7)
                    .registerClientbound(BlockDestructionPacket.class, BlockDestructionSerializer_v754.INSTANCE, 8)
                    .registerClientbound(BlockEntityDataPacket.class, BlockEntityDataSerializer_v754.INSTANCE, 9)
                    .registerClientbound(BlockEventPacket.class, BlockEventSerializer_v754.INSTANCE, 10)
                    .registerClientbound(BlockUpdatePacket.class, BlockUpdateSerializer_v754.INSTANCE, 11)
                    .registerClientbound(BossEventPacket.class, BossEventSerializer_v754.INSTANCE, 12)
                    .registerClientbound(ChangeDifficultyPacket.class, ChangeDifficultySerializer_v754.INSTANCE, 13)
                    .registerClientbound(ServerChatPacket.class, ServerChatSerializer_v754.INSTANCE, 14)
                    .registerClientbound(CommandSuggestionsPacket.class, CommandSuggestionsSerializer_v754.INSTANCE, 15)
                    .registerClientbound(CommandsPacket.class, CommandsSerializer_v754.INSTANCE, 16)
                    .registerClientbound(ContainerAckPacket.class, ContainerAckSerializer_v754.INSTANCE, 17)
                    .registerClientbound(ContainerClosePacket.class, ContainerCloseSerializer_v754.INSTANCE, 18)
                    .registerClientbound(ContainerSetContentPacket.class, ContainerSetContentSerializer_v754.INSTANCE, 19)
                    .registerClientbound(ContainerSetDataPacket.class, ContainerSetDataSerializer_v754.INSTANCE, 20)
                    .registerClientbound(ContainerSetSlotPacket.class, ContainerSetSlotSerializer_v754.INSTANCE, 21)
                    .registerClientbound(CooldownPacket.class, CooldownSerializer_v754.INSTANCE, 22)
                    .registerClientbound(CustomPayloadPacket.class, CustomPayloadSerializer_v754.INSTANCE, 23)
                    .registerClientbound(CustomSoundPacket.class, CustomSoundSerializer_v754.INSTANCE, 24)
                    .registerClientbound(org.cloudburstmc.protocol.java.packet.play.clientbound.DisconnectPacket.class, org.cloudburstmc.protocol.java.v754.serializer.play.clientbound.DisconnectSerializer_v754.INSTANCE, 25)
                    .registerClientbound(EntityEventPacket.class, EntityEventSerializer_v754.INSTANCE, 26)
                    .registerClientbound(ExplodePacket.class, ExplodeSerializer_v754.INSTANCE, 27)
                    .registerClientbound(ForgetLevelChunkPacket.class, ForgetLevelChunkSerializer_v754.INSTANCE, 28)
                    .registerClientbound(GameEventPacket.class, GameEventSerializer_v754.INSTANCE, 29)
                    .registerClientbound(HorseScreenOpenPacket.class, HorseScreenOpenSerializer_v754.INSTANCE, 30)
                    .registerClientbound(KeepAlivePacket.class, KeepAliveSerializer_v754.INSTANCE, 31)
                    .registerClientbound(LevelChunkPacket.class, LevelChunkSerializer_v754.INSTANCE, 32)
                    .registerClientbound(LevelEventPacket.class, LevelEventSerializer_v754.INSTANCE, 33)
                    .registerClientbound(LoginPacket.class, LoginSerializer_v754.INSTANCE, 36)
                    .registerClientbound(MapItemDataPacket.class, MapItemDataSerializer_v754.INSTANCE, 37)
                    .registerClientbound(MerchantOffersPacket.class, MerchantOffersSerializer_v754.INSTANCE, 38)
                    .registerClientbound(PlayerPositionPacket.class, PlayerPositionSerializer_v754.INSTANCE, 52)
                    .registerClientbound(SetCarriedItemPacket.class, SetCarriedItemSerializer_v754.INSTANCE, 63)

                    .registerServerbound(AcceptTeleportationPacket.class, AcceptTeleportationSerializer_v754.INSTANCE, 0)
                    .registerServerbound(ClientChatPacket.class, ClientChatSerializer_v754.INSTANCE, 3)
                    .registerServerbound(ClientCommandPacket.class, ClientCommandSerializer_v754.INSTANCE, 4)
                    .registerServerbound(ClientInformationPacket.class, ClientInformationSerializer_v754.INSTANCE, 5)
                    .registerServerbound(ContainerClickPacket.class, ContainerClickSerializer_v754.INSTANCE, 9)
                    .registerServerbound(ContainerClosePacket.class, ContainerCloseSerializer_v754.INSTANCE, 10)
                    .registerServerbound(CustomPayloadPacket.class, CustomPayloadSerializer_v754.INSTANCE, 11)
                    .registerServerbound(KeepAlivePacket.class, KeepAliveSerializer_v754.INSTANCE, 16)
                    .registerServerbound(MovePlayerPacket.Pos.class, MovePlayerSerializer_v754.Pos.INSTANCE, 18)
                    .registerServerbound(MovePlayerPacket.PosRot.class, MovePlayerSerializer_v754.PosRot.INSTANCE, 19)
                    .registerServerbound(MovePlayerPacket.Rot.class, MovePlayerSerializer_v754.Rot.INSTANCE, 20)
                    .registerServerbound(PlayerActionPacket.class, PlayerActionSerializer_v754.INSTANCE, 27)
                    .registerServerbound(RecipeBookChangeSettingsPacket.class, RecipeBookChangeSettingsSerializer_v754.INSTANCE, 30)
                    .registerServerbound(SeenAdvancementsPacket.class, SeenAdvancementsSerializer_v754.INSTANCE, 34)
                    .registerServerbound(SetCarriedItemPacket.class, SetCarriedItemSerializer_v754.INSTANCE, 37)
                    .build()
            ).build();
}
