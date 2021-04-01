package com.nukkitx.protocol.genoa.packet;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector4f;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketType;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class GenoaItemParticlePacket extends BedrockPacket {

    public int particleId;
    public int dimensionId;
    public Vector3f position;
    public long uniqueEntityId;

    @Override
    public final boolean handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    public BedrockPacketType getPacketType() {
        return BedrockPacketType.GENOA_ITEM_PARTICLE;
    }
}