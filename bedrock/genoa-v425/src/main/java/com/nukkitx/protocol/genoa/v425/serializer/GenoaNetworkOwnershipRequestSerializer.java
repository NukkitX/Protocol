package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaNetworkOwnershipRequestPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaNetworkOwnershipRequestSerializer implements BedrockPacketSerializer<GenoaNetworkOwnershipRequestPacket> {
    public static final GenoaNetworkOwnershipRequestSerializer INSTANCE = new GenoaNetworkOwnershipRequestSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaNetworkOwnershipRequestPacket packet) {
        buffer.writeByte(packet.getByte1());
        VarInts.writeUnsignedLong(buffer,packet.getUnsignedVarLong());
        buffer.writeBoolean(packet.isB1());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaNetworkOwnershipRequestPacket packet) {
        packet.setByte1(buffer.readByte());
        packet.setUnsignedVarLong(VarInts.readUnsignedLong(buffer));
        packet.setB1(buffer.readBoolean());
    }
}


