package org.cloudburstmc.protocol.java.pipeline;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.util.Zlib;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import org.cloudburstmc.protocol.java.JavaSession;

import java.util.zip.Deflater;

@AllArgsConstructor
public class PacketCompressor extends MessageToByteEncoder<ByteBuf> {

    private final JavaSession session;

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf in, ByteBuf out) {
        int length = in.readableBytes();
        if (length < this.session.getCompressionThreshold()) {
            VarInts.writeUnsignedInt(out, 0);
            out.writeBytes(in);
            return;
        }

        VarInts.writeUnsignedInt(out, in.readableBytes());
        Zlib.DEFAULT.deflate(in, out, Deflater.DEFAULT_COMPRESSION);
    }
}
