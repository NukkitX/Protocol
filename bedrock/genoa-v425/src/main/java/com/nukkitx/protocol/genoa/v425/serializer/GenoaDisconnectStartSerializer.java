package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaDisconnectStart;
import com.nukkitx.protocol.genoa.packet.GenoaNetworkOwnershipStatusPacket;
import io.netty.buffer.ByteBuf;

public class GenoaDisconnectStartSerializer implements BedrockPacketSerializer<GenoaDisconnectStart> {
    public static final GenoaDisconnectStartSerializer INSTANCE = new GenoaDisconnectStartSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaDisconnectStart packet) {
        buffer.writeByte(packet.getByte1());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaDisconnectStart packet) {
        packet.setByte1(buffer.readByte());
    }
}
