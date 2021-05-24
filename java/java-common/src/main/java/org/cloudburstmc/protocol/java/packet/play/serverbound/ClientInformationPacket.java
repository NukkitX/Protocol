package org.cloudburstmc.protocol.java.packet.play.serverbound;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.data.Hand;
import org.cloudburstmc.protocol.java.data.text.ChatVisibility;
import org.cloudburstmc.protocol.java.handler.JavaPlayPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaPlayPacketType;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class ClientInformationPacket extends JavaPacket<JavaPlayPacketHandler> {
    private String language;
    private int viewDistance;
    private ChatVisibility visibility;
    private boolean chatColors;
    private int modelCustomisation;
    private Hand mainHand;

    @Override
    public boolean handle(JavaPlayPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaPlayPacketType.CLIENT_INFORMATION_C2S;
    }
}
