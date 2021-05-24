package org.cloudburstmc.protocol.java.pipeline;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.JavaPacketCodec;
import org.cloudburstmc.protocol.java.JavaServerSession;
import org.cloudburstmc.protocol.java.JavaSession;

public class PacketEncoder extends MessageToByteEncoder<JavaPacket<?>> {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(PacketEncoder.class);

    private final JavaSession session;
    private final boolean clientbound;

    public PacketEncoder(JavaSession session) {
        this.session = session;
        // A server sends packets that a client has to handle
        clientbound = session instanceof JavaServerSession;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, JavaPacket<?> packet, ByteBuf out) {
        JavaPacketCodec.JavaStateCodec stateCodec = this.session.getPacketCodec().getCodec(packet.getPacketType().getState());
        //todo the following line to tryEncode
        int packetId = stateCodec.getId(packet, clientbound);
        try {
            VarInts.writeUnsignedInt(out, packetId);
            stateCodec.tryEncode(out, packet, this.session, clientbound);
        } catch (Throwable ex) {
            log.error("Error encoding packet: {}", packetId, ex);
        }
    }
}
