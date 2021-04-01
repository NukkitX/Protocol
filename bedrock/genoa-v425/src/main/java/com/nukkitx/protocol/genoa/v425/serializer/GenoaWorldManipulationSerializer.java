package com.nukkitx.protocol.genoa.v425.serializer;

import com.nukkitx.protocol.bedrock.BedrockPacketHelper;
import com.nukkitx.protocol.bedrock.BedrockPacketSerializer;
import com.nukkitx.protocol.genoa.packet.*;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public class GenoaWorldManipulationSerializer implements BedrockPacketSerializer<GenoaWorldManipulationPacket> {
        public static final GenoaWorldManipulationSerializer INSTANCE = new GenoaWorldManipulationSerializer();

        @Override
        public void serialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaWorldManipulationPacket packet) {
            buffer.writeByte(packet.getIsWorldTicking());
        }

        @Override
        public void deserialize(ByteBuf buffer, BedrockPacketHelper helper, GenoaWorldManipulationPacket packet) {
            packet.setIsWorldTicking(buffer.readByte());
        }
    }


