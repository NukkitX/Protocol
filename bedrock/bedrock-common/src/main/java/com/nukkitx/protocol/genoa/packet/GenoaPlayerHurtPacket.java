package com.nukkitx.protocol.genoa.packet;

import com.nukkitx.math.vector.Vector4f;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketType;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class GenoaPlayerHurtPacket extends BedrockPacket {

    public int Int1;
    public int Int2;
    public float Float1;
    public float Float2;
    public float Float3;
    public float Float4;

    @Override
    public final boolean handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    public BedrockPacketType getPacketType() {
        return BedrockPacketType.GENOA_PLAYER_HURT;
    }
}
