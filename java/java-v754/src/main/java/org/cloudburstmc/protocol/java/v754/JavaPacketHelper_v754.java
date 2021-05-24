package org.cloudburstmc.protocol.java.v754;

import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.data.entity.EntityDataType;
import org.cloudburstmc.protocol.java.data.entity.Pose;
import org.cloudburstmc.protocol.java.data.inventory.ContainerType;

import static org.cloudburstmc.protocol.java.data.entity.EntityType.*;
import static org.cloudburstmc.protocol.java.data.inventory.ContainerType.*;
import static org.cloudburstmc.protocol.java.data.world.BlockEntityAction.*;

public class JavaPacketHelper_v754 extends JavaPacketHelper {
    public static final JavaPacketHelper INSTANCE = new JavaPacketHelper_v754();

    @Override
    protected void registerEntityTypes() {
        this.entityTypes.put(0, AREA_EFFECT_CLOUD);
        this.entityTypes.put(1, ARMOR_STAND);
        this.entityTypes.put(2, ARROW);
        this.entityTypes.put(3, BAT);
        this.entityTypes.put(4, BEE);
        this.entityTypes.put(5, BOAT);
        this.entityTypes.put(6, BLAZE);
        this.entityTypes.put(7, CAT);
        this.entityTypes.put(8, CAVE_SPIDER);
        this.entityTypes.put(9, CHICKEN);
        this.entityTypes.put(10, COD);
        this.entityTypes.put(11, COW);
        this.entityTypes.put(12, CREEPER);
        this.entityTypes.put(13, DONKEY);
        this.entityTypes.put(14, DOLPHIN);
        this.entityTypes.put(15, DRAGON_FIREBALL);
        this.entityTypes.put(16, DROWNED);
        this.entityTypes.put(17, ELDER_GUARDIAN);
        this.entityTypes.put(18, END_CRYSTAL);
        this.entityTypes.put(19, ENDER_DRAGON);
        this.entityTypes.put(20, ENDERMAN);
        this.entityTypes.put(21, ENDERMITE);
        this.entityTypes.put(22, EVOKER_FANGS);
        this.entityTypes.put(23, EVOKER);
        this.entityTypes.put(24, EXPERIENCE_ORB);
        this.entityTypes.put(25, EYE_OF_ENDER);
        this.entityTypes.put(26, FALLING_BLOCK);
        this.entityTypes.put(27, FIREWORK_ROCKET);
        this.entityTypes.put(28, FOX);
        this.entityTypes.put(29, GHAST);
        this.entityTypes.put(30, GIANT);
        this.entityTypes.put(31, GUARDIAN);
        this.entityTypes.put(32, HORSE);
        this.entityTypes.put(33, HUSK);
        this.entityTypes.put(34, ILLUSIONER);
        this.entityTypes.put(35, ITEM);
        this.entityTypes.put(36, ITEM_FRAME);
        this.entityTypes.put(37, FIREBALL);
        this.entityTypes.put(38, LEASH_KNOT);
        this.entityTypes.put(39, LIGHTNING_BOLT);
        this.entityTypes.put(40, LLAMA);
        this.entityTypes.put(41, LLAMA_SPIT);
        this.entityTypes.put(42, MAGMA_CUBE);
        this.entityTypes.put(43, MINECART);
        this.entityTypes.put(44, MINECART_CHEST);
        this.entityTypes.put(45, MINECART_COMMAND_BLOCK);
        this.entityTypes.put(46, MINECART_FURNACE);
        this.entityTypes.put(47, MINECART_HOPPER);
        this.entityTypes.put(48, MINECART_SPAWNER);
        this.entityTypes.put(49, MINECART_TNT);
        this.entityTypes.put(50, MULE);
        this.entityTypes.put(51, MOOSHROOM);
        this.entityTypes.put(52, OCELOT);
        this.entityTypes.put(53, PAINTING);
        this.entityTypes.put(54, PANDA);
        this.entityTypes.put(55, PARROT);
        this.entityTypes.put(56, PIG);
        this.entityTypes.put(57, PIGLIN);
        this.entityTypes.put(58, PIGLIN_BRUTE);
        this.entityTypes.put(59, PUFFERFISH);
        this.entityTypes.put(60, POLAR_BEAR);
        this.entityTypes.put(61, PRIMED_TNT);
        this.entityTypes.put(62, RABBIT);
        this.entityTypes.put(63, SALMON);
        this.entityTypes.put(64, SHEEP);
        this.entityTypes.put(65, SHULKER);
        this.entityTypes.put(66, SHULKER_BULLET);
        this.entityTypes.put(67, SILVERFISH);
        this.entityTypes.put(68, SKELETON);
        this.entityTypes.put(69, SKELETON_HORSE);
        this.entityTypes.put(70, SLIME);
        this.entityTypes.put(71, SMALL_FIREBALL);
        this.entityTypes.put(72, SNOW_GOLEM);
        this.entityTypes.put(73, SNOWBALL);
        this.entityTypes.put(74, SPECTRAL_ARROW);
        this.entityTypes.put(75, SPIDER);
        this.entityTypes.put(76, SQUID);
        this.entityTypes.put(77, STRAY);
        this.entityTypes.put(78, TRADER_LLAMA);
        this.entityTypes.put(79, TROPICAL_FISH);
        this.entityTypes.put(80, TURTLE);
        this.entityTypes.put(81, THROWN_EGG);
        this.entityTypes.put(82, THROWN_ENDERPEARL);
        this.entityTypes.put(83, THROWN_EXP_BOTTLE);
        this.entityTypes.put(84, THROWN_POTION);
        this.entityTypes.put(85, TRIDENT);
        this.entityTypes.put(86, VEX);
        this.entityTypes.put(87, VILLAGER);
        this.entityTypes.put(88, IRON_GOLEM);
        this.entityTypes.put(89, VINDICATOR);
        this.entityTypes.put(90, PILLAGER);
        this.entityTypes.put(91, WANDERING_TRADER);
        this.entityTypes.put(92, WITCH);
        this.entityTypes.put(93, WITHER);
        this.entityTypes.put(94, WITHER_SKELETON);
        this.entityTypes.put(95, WITHER_SKULL);
        this.entityTypes.put(96, WOLF);
        this.entityTypes.put(97, ZOMBIE);
        this.entityTypes.put(98, ZOMBIE_HORSE);
        this.entityTypes.put(99, ZOMBIFIED_PIGLIN);
        this.entityTypes.put(100, ZOMBIE_VILLAGER);
        this.entityTypes.put(101, PHANTOM);
        this.entityTypes.put(102, RAVAGER);
        this.entityTypes.put(103, HOGLIN);
        this.entityTypes.put(104, STRIDER);
        this.entityTypes.put(105, ZOGLIN);
        this.entityTypes.put(106, PLAYER);
        this.entityTypes.put(107, FISHING_BOBBER);
    }

    @Override
    protected void registerBlockEntityActions() {
        this.blockEntityActions.put(1, MOB_SPAWNER_DATA);
        this.blockEntityActions.put(2, COMMAND_BLOCK_TEXT);
        this.blockEntityActions.put(3, BEACON_LEVEL);
        this.blockEntityActions.put(4, HEAD_ROTATION);
        this.blockEntityActions.put(5, DECLARE_CONDUIT);
        this.blockEntityActions.put(6, BANNER_COLOR);
        this.blockEntityActions.put(7, STRUCTURE_DATA);
        this.blockEntityActions.put(8, GATEWAY_DESTINATION);
        this.blockEntityActions.put(9, SIGN_TEXT);
        this.blockEntityActions.put(11, DECLARE_BED);
        this.blockEntityActions.put(12, JIGSAW_DATA);
        this.blockEntityActions.put(13, CAMPFIRE_ITEMS);
        this.blockEntityActions.put(14, BEEHIVE_INFO);
    }

    @Override
    protected void registerContainerTypes() {
        this.containerTypes.put(0, GENERIC_9X1);
        this.containerTypes.put(1, GENERIC_9X2);
        this.containerTypes.put(2, GENERIC_9X3);
        this.containerTypes.put(3, GENERIC_9X4);
        this.containerTypes.put(4, GENERIC_9X5);
        this.containerTypes.put(5, GENERIC_9X6);
        this.containerTypes.put(6, GENERIC_3X3);
        this.containerTypes.put(7, ANVIL);
        this.containerTypes.put(8, BEACON);
        this.containerTypes.put(9, BLAST_FURNACE);
        this.containerTypes.put(10, BREWING_STAND);
        this.containerTypes.put(11, CRAFTING);
        this.containerTypes.put(12, ENCHANTMENT);
        this.containerTypes.put(13, FURNACE);
        this.containerTypes.put(14, GRINDSTONE);
        this.containerTypes.put(15, HOPPER);
        this.containerTypes.put(16, LECTERN);
        this.containerTypes.put(17, LOOM);
        this.containerTypes.put(18, MERCHANT);
        this.containerTypes.put(19, SHULKER_BOX);
        this.containerTypes.put(20, SMITHING);
        this.containerTypes.put(21, SMOKER);
        this.containerTypes.put(22, CARTOGRAPHY_TABLE);
        this.containerTypes.put(23, STONECUTTER);
    }

    @Override
    protected void registerEntityDataTypes() {
        this.entityDataTypes.put(0, EntityDataType.BYTE);
        this.entityDataTypes.put(1, EntityDataType.INT);
        this.entityDataTypes.put(2, EntityDataType.FLOAT);
        this.entityDataTypes.put(3, EntityDataType.STRING);
        this.entityDataTypes.put(4, EntityDataType.COMPONENT);
        this.entityDataTypes.put(5, EntityDataType.OPTIONAL_COMPONENT);
        this.entityDataTypes.put(6, EntityDataType.ITEM_STACK);
        this.entityDataTypes.put(7, EntityDataType.BOOLEAN);
        this.entityDataTypes.put(8, EntityDataType.ROTATION);
        this.entityDataTypes.put(9, EntityDataType.BLOCK_POS);
        this.entityDataTypes.put(10, EntityDataType.OPTIONAL_BLOCK_POS);
        this.entityDataTypes.put(11, EntityDataType.DIRECTION);
        this.entityDataTypes.put(12, EntityDataType.OPTIONAL_UUID);
        this.entityDataTypes.put(13, EntityDataType.OPTIONAL_BLOCK_STATE);
        this.entityDataTypes.put(14, EntityDataType.NBT);
        this.entityDataTypes.put(15, EntityDataType.PARTICLE);
        this.entityDataTypes.put(16, EntityDataType.VILLAGER_DATA);
        this.entityDataTypes.put(17, EntityDataType.OPTIONAL_INT);
        this.entityDataTypes.put(18, EntityDataType.POSE);
    }

    @Override
    public void registerPoses() {
        this.poses.put(0, Pose.STANDING);
        this.poses.put(1, Pose.FALL_FLYING);
        this.poses.put(2, Pose.SLEEPING);
        this.poses.put(3, Pose.SWIMMING);
        this.poses.put(4, Pose.SPIN_ATTACK);
        this.poses.put(5, Pose.CROUCHING);
        this.poses.put(6, Pose.DYING);
    }
}
