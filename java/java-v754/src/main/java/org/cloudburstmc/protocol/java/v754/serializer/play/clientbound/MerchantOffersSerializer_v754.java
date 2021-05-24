package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.protocol.exception.PacketSerializeException;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.MerchantOffer;
import org.cloudburstmc.protocol.java.packet.play.clientbound.MerchantOffersPacket;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MerchantOffersSerializer_v754 implements JavaPacketSerializer<MerchantOffersPacket> {
    public static final MerchantOffersSerializer_v754 INSTANCE = new MerchantOffersSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, MerchantOffersPacket packet) throws PacketSerializeException {
        helper.writeVarInt(buffer, packet.getContainerId());
        buffer.writeByte(packet.getOffers().size());
        for (MerchantOffer offer : packet.getOffers()) {
            helper.writeItemStack(buffer, offer.getBuyA());
            helper.writeItemStack(buffer, offer.getSell());
            helper.writeOptional(buffer, offer.getBuyB(), helper::writeItemStack);
            buffer.writeBoolean(offer.isOutOfStock());
            buffer.writeInt(offer.getUses());
            buffer.writeInt(offer.getMaxUses());
            buffer.writeInt(offer.getXp());
            buffer.writeInt(offer.getSpecialPriceDiff());
            buffer.writeFloat(offer.getPriceMultiplier());
            buffer.writeInt(offer.getDemand());
        }
        helper.writeVarInt(buffer, packet.getVillagerLevel());
        helper.writeVarInt(buffer, packet.getVillagerXp());
        buffer.writeBoolean(packet.isShowProgress());
        buffer.writeBoolean(packet.isCanRestock());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, MerchantOffersPacket packet) throws PacketSerializeException {
        packet.setContainerId(helper.readVarInt(buffer));
        List<MerchantOffer> offers = new ObjectArrayList<>();
        for (int i = 0; i < buffer.readByte(); i++) {
            offers.add(new MerchantOffer(
               helper.readItemStack(buffer),
               helper.readItemStack(buffer),
               helper.readOptional(buffer, helper::readItemStack),
               buffer.readBoolean(),
               buffer.readInt(),
               buffer.readInt(),
               buffer.readInt(),
               buffer.readInt(),
               buffer.readFloat(),
               buffer.readInt()
            ));
        }
        packet.setOffers(offers);
        packet.setVillagerLevel(helper.readVarInt(buffer));
        packet.setVillagerXp(helper.readVarInt(buffer));
        packet.setShowProgress(buffer.readBoolean());
        packet.setCanRestock(buffer.readBoolean());
    }
}
