package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.PersonaMobRequestPacket;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonaMobRequestSeralizer implements BedrockPacketSerializer<PersonaMobRequestPacket> {
    public static final PersonaMobRequestSeralizer INSTANCE = new PersonaMobRequestSeralizer();

    @Override
    public void serialize(ByteBuf buffer, BedrockPacketHelper helper, PersonaMobRequestPacket packet) {
        VarInts.writeUnsignedLong(buffer,packet.getUnsignedLong1());
        helper.writeString(buffer,packet.getS1());
        helper.writeString(buffer,packet.getS2());
    }

    @Override
    public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, PersonaMobRequestPacket packet) {
        packet.setUnsignedLong1(VarInts.readUnsignedLong(buffer));
        packet.setS1(helper.readString(buffer));
        packet.setS2(helper.readString(buffer));
    }
}


