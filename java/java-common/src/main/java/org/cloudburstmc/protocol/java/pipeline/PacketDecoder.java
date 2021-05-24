package org.cloudburstmc.protocol.java.pipeline;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.AllArgsConstructor;
import org.cloudburstmc.protocol.java.JavaSession;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;

import java.util.List;

@AllArgsConstructor
public class PacketDecoder extends ByteToMessageDecoder {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(PacketEncoder.class);

    private final JavaSession session;
    private final JavaPacketType.Direction direction;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!ctx.channel().isActive()) {
            return;
        }
        int packetId = VarInts.readUnsignedInt(in);
        try {
            out.add(this.session.getPacketCodec().getCodec(this.session.getProtocolState()).tryDecode(in, packetId, this.session, this.direction));
        } catch (Throwable ex) {
            log.error("Error decoding packet: {}", packetId, ex);
        }
    }
}
