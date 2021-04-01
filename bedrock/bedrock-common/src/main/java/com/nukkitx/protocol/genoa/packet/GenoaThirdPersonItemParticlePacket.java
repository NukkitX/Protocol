package com.nukkitx.protocol.genoa.packet;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketType;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class GenoaThirdPersonItemParticlePacket extends BedrockPacket {

    public int particleId;
    public int dimensionId;
    public Vector3f position; // Could also be flipped
    public Vector3f viewDirection;
    public long unsignedVarLong1;

    @Override
    public final boolean handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    public BedrockPacketType getPacketType() {
        return BedrockPacketType.GENOA_THIRD_PERSON_ITEM_PARTICLE;
    }
}