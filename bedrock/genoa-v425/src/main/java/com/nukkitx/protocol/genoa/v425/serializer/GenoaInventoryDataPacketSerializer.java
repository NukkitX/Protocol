package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaInventoryDataPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaInventoryDataPacketSerializer implements BedrockPacketSerializer<GenoaInventoryDataPacket> {
    public static final GenoaInventoryDataPacketSerializer INSTANCE = new GenoaInventoryDataPacketSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaInventoryDataPacket packet) {
        helper.writeString(buffer,packet.getJson());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaInventoryDataPacket packet) {
        packet.setJson(helper.readString(buffer));
    }
}


