package com.nukkitx.protocol.bedrock.handler;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockSession;
import com.nukkitx.protocol.handler.BatchHandler;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultBatchHandler implements BatchHandler<BedrockSession, BedrockPacket> {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(DefaultBatchHandler.class);
    public static final DefaultBatchHandler INSTANCE = new DefaultBatchHandler();

    @Override
    public void handle(BedrockSession session, ByteBuf compressed, Collection<BedrockPacket> packets) {
        for (BedrockPacket packet : packets) {
            if (session.isLogging() && log.isTraceEnabled()) {
                log.trace("Inbound {}: {}", session.getAddress(), packet);
            }

            BedrockPacketHandler handler = session.getPacketHandler();
            boolean release = true;
            try {
                if (handler != null && packet.handle(handler)) {
                    release = false;
                } else {
                    log.debug("Unhandled packet for {}: {}", session.getAddress(), packet);
                }
            } finally {
                if (release) {
                    ReferenceCountUtil.release(packet);
                }
            }
        }
    }
}
