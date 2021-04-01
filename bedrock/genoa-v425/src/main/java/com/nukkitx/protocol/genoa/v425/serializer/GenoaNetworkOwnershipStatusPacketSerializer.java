package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.bedrock.data.NetworkOwnershipStatus;
import com.nukkitx.protocol.genoa.packet.GenoaNetworkOwnershipStatusPacket;
import io.netty.buffer.ByteBuf;

public class GenoaNetworkOwnershipStatusPacketSerializer implements BedrockPacketSerializer<GenoaNetworkOwnershipStatusPacket> {
    public static final GenoaNetworkOwnershipStatusPacketSerializer INSTANCE = new GenoaNetworkOwnershipStatusPacketSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaNetworkOwnershipStatusPacket packet) {
        helper.writeArray(buffer,packet.getArr(), (buf,help,pac) -> {
            buf.writeByte(pac.getByte1());
            VarInts.writeUnsignedLong(buf,pac.getUnsignedLong1());
        });
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaNetworkOwnershipStatusPacket packet) {
        helper.readArray(buffer, packet.getArr(), (byteBuf, bedrockPacketHelper) -> {
            byte Byte1 = buffer.readByte();
            long UnsignedLong1 = VarInts.readUnsignedLong(buffer);
            return new NetworkOwnershipStatus(Byte1,UnsignedLong1);
        });
    }
}
