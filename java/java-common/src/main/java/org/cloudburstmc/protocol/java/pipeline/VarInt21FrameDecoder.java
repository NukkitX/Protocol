package org.cloudburstmc.protocol.java.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.util.List;

public class VarInt21FrameDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (!ctx.channel().isActive()) {
            in.skipBytes(in.readableBytes());
            return;
        }
        in.markReaderIndex();
        int length = 0;
        for (int shift = 0; shift < 24; shift += 7) {
            if (!in.isReadable()) {
                in.resetReaderIndex();
                return;
            }
            byte b = in.readByte();
            length |= (b & 0x7FL) << shift;
            if (b < 0) {
                continue;
            }
            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }
            out.add(in.readBytes(length));
            return;
        }
        throw new CorruptedFrameException("VarInt length wider than 21-bit!");
    }
}
