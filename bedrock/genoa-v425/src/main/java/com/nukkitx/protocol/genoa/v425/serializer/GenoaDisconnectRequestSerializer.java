package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaDisconnectRequest;
import com.nukkitx.protocol.genoa.packet.GenoaNetworkOwnershipStatusPacket;
import io.netty.buffer.ByteBuf;

public class GenoaDisconnectRequestSerializer implements BedrockPacketSerializer<GenoaDisconnectRequest> {
    public static final GenoaDisconnectRequestSerializer INSTANCE = new GenoaDisconnectRequestSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaDisconnectRequest packet) {

    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaDisconnectRequest packet) {

    }
}
