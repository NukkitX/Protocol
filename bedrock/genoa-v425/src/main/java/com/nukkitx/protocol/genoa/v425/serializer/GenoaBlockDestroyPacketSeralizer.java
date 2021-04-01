package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaBlockDestroyPacket;
import com.nukkitx.protocol.genoa.packet.PersonaMobRequestPacket;
import io.netty.buffer.ByteBuf;

public class GenoaBlockDestroyPacketSeralizer implements BedrockPacketSerializer<GenoaBlockDestroyPacket> {

    public static final GenoaBlockDestroyPacketSeralizer INSTANCE = new GenoaBlockDestroyPacketSeralizer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaBlockDestroyPacket packet) {
        VarInts.writeUnsignedLong(buffer,packet.getRuntimeEntityId());
        helper.writeUuid(buffer,packet.getItemCatalogUuid());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaBlockDestroyPacket packet) {
        packet.setRuntimeEntityId(VarInts.readUnsignedLong(buffer));
        packet.setItemCatalogUuid(helper.readUuid(buffer));
    }
}
