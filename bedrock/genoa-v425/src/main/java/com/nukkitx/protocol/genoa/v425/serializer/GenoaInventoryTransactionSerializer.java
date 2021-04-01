package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.bedrock.data.inventory.LegacySetItemSlotData;
import com.nukkitx.protocol.bedrock.data.inventory.TransactionType;
import com.nukkitx.protocol.bedrock.packet.InventoryTransactionPacket;
import com.nukkitx.protocol.bedrock.v291.serializer.InventoryTransactionSerializer_v291;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaInventoryTransactionSerializer extends InventoryTransactionSerializer_v291 {
    public static final GenoaInventoryTransactionSerializer INSTANCE = new GenoaInventoryTransactionSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, InventoryTransactionPacket packet, BedrockSession session) {

        // TODO: Still not working, needs proper fixing

        TransactionType transactionType = packet.getTransactionType();
        VarInts.writeUnsignedInt(buffer, transactionType.ordinal());

        //buffer.writeBoolean(packet.isHasNetworkIds());
        //helper.writeArray(buffer, packet.getActions(), (buf, action) -> helper.writeInventoryAction(buf, action, session, packet.isHasNetworkIds()));

        helper.writeArray(buffer, packet.getActions(), (buf, action) -> helper.writeInventoryAction(buf, action, session, false));

        switch (transactionType) {
            case ITEM_USE:
                helper.writeItemUse(buffer, packet, session);
                break;
            case ITEM_USE_ON_ENTITY:
                this.writeItemUseOnEntity(buffer, helper, packet, session);
                break;
            case ITEM_RELEASE:
                this.writeItemRelease(buffer, helper, packet, session);
                break;
        }
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, InventoryTransactionPacket packet, BedrockSession session) {

        TransactionType transactionType = TransactionType.values()[VarInts.readUnsignedInt(buffer)];
        packet.setTransactionType(transactionType);

        //boolean hasNetworkIds = buffer.readBoolean();
        //packet.setHasNetworkIds(hasNetworkIds);

        helper.readArray(buffer, packet.getActions(), buf -> helper.readInventoryAction(buf, session, false));

        switch (transactionType) {
            case ITEM_USE:
                helper.readItemUse(buffer, packet, session);
                break;
            case ITEM_USE_ON_ENTITY:
                this.readItemUseOnEntity(buffer, helper, packet, session);
                break;
            case ITEM_RELEASE:
                this.readItemRelease(buffer, helper, packet, session);
                break;
        }
    }
}
