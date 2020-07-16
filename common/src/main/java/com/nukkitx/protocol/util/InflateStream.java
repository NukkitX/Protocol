package com.nukkitx.protocol.util;

import java.util.zip.DataFormatException;

import com.nukkitx.natives.zlib.Inflater;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InflateStream {
    protected static final InternalLogger log = InternalLoggerFactory.getInstance(InflateStream.class);

    Inflater inflater;
    ByteBuf source;
    boolean doRelease;

    public InflateStream(Inflater inflater, ByteBuf source, boolean doRelease) {
        this.inflater = inflater;
        this.source = source;
        this.doRelease = doRelease;
    }

    public boolean finished() {
        return inflater.finished();
    }

    public int inflateChunk(ByteBuf decompressed, int chunk) throws DataFormatException {
        try {
            decompressed.ensureWritable(chunk);
            int index = decompressed.writerIndex();
            int written = inflater.inflate(decompressed.internalNioBuffer(index, chunk));
            decompressed.writerIndex(index + written);

            source.readerIndex((int)inflater.getBytesRead());

            return written;
        } catch (DataFormatException e) {
            decompressed.release();
            throw e;
        }
    }

    public void free() {
        if (source.readableBytes() > 0) {
            if (log.isTraceEnabled()) {
                log.trace("Source not empty! {}", source);
                ByteBuf hexdump = source.readBytes(source.readableBytes());
                log.trace(ByteBufUtil.prettyHexDump(hexdump));
                hexdump.release();
            }
        }
        if (doRelease) {
            source.release();
        }
    }
}
