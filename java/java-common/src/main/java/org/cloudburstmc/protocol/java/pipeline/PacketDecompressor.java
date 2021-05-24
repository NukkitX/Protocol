package org.cloudburstmc.protocol.java.pipeline;

import com.nukkitx.network.VarInts;
import com.nukkitx.protocol.util.Zlib;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import lombok.AllArgsConstructor;
import org.cloudburstmc.protocol.java.JavaSession;

import java.util.List;

@AllArgsConstructor
public class PacketDecompressor extends ByteToMessageDecoder {
    private static final int MAX_COMPRESSED_SIZE = 0x200000;

    private final JavaSession session;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() == 0) {
            return;
        }
        int size = VarInts.readUnsignedInt(in);
        if (size == 0) {
            out.add(in.readBytes(in.readableBytes()));
            return;
        }
        if (size < this.session.getCompressionThreshold()) {
            throw new DecoderException(String.format("Badly compressed packet. Size %s is below server threshold of %s!", size, this.session.getCompressionThreshold()));
        }
        Zlib.DEFAULT.inflate(in, MAX_COMPRESSED_SIZE);
    }
}
