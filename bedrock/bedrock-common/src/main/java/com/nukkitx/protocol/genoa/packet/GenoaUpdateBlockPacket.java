package com.nukkitx.protocol.genoa.packet;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockPacketType;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.UpdateBlockPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class GenoaUpdateBlockPacket extends BedrockPacket {

    public static final Set<UpdateBlockPacket.Flag> FLAG_ALL = Collections.unmodifiableSet(EnumSet.of(UpdateBlockPacket.Flag.NEIGHBORS, UpdateBlockPacket.Flag.NETWORK));
    public static final Set<UpdateBlockPacket.Flag> FLAG_ALL_PRIORITY = Collections.unmodifiableSet(
            EnumSet.of(UpdateBlockPacket.Flag.NEIGHBORS, UpdateBlockPacket.Flag.NETWORK, UpdateBlockPacket.Flag.PRIORITY));

    private final Set<UpdateBlockPacket.Flag> flags = EnumSet.noneOf(UpdateBlockPacket.Flag.class);
    private Vector3i blockPosition;
    private int runtimeId;
    private int dataLayer;

    public float Float1;
    public boolean CheckForFloat;

    @Override
    public final boolean handle(BedrockPacketHandler handler) {
        return handler.handle(this);
    }

    public BedrockPacketType getPacketType() {
        return BedrockPacketType.GENOA_UPDATE_BLOCK;
    }
}
