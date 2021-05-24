package org.cloudburstmc.protocol.java.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.RequiredArgsConstructor;

import javax.crypto.Cipher;
import java.util.List;

@RequiredArgsConstructor
public class PacketDecrypter extends MessageToMessageDecoder<ByteBuf> {
    private final Cipher cipher;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // TODO: Native encryption?
        // Encryption is copy safe so we can use the same buffer.
        this.cipher.update(in.retain().internalNioBuffer(in.readerIndex(), in.readableBytes()),
                in.internalNioBuffer(in.readerIndex(), in.readableBytes()).slice());

        out.add(in);
    }
}
