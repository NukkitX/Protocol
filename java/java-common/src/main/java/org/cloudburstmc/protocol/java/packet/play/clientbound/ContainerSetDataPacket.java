package org.cloudburstmc.protocol.java.packet.play.clientbound;

import lombok.*;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.handler.JavaPlayPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPlayPacketType;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
@AllArgsConstructor
public class ContainerSetDataPacket extends JavaPacket<JavaPlayPacketHandler> {
    private int containerId;
    private int property;
    private int value;

    @Override
    public boolean handle(JavaPlayPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPlayPacketType getPacketType() {
        return JavaPlayPacketType.CONTAINER_SET_DATA_S2C;
    }
}
