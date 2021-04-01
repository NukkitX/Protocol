package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.GenoaSetActorMolangVariablesPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenoaSetActorMolangVariablesSerializer implements BedrockPacketSerializer<GenoaSetActorMolangVariablesPacket> {
    public static final GenoaSetActorMolangVariablesSerializer INSTANCE = new GenoaSetActorMolangVariablesSerializer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaSetActorMolangVariablesPacket packet) {
        buffer.writeLongLE(packet.getUint64());
        VarInts.writeUnsignedInt(buffer,packet.getU_varint());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaSetActorMolangVariablesPacket packet) {
        packet.setUint64(buffer.readLongLE());
        packet.setU_varint(VarInts.readUnsignedInt(buffer));
    }
}


