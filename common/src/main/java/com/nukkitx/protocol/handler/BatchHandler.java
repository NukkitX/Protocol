package com.nukkitx.protocol.handler;

import com.nukkitx.protocol.MinecraftSession;
import io.netty.buffer.ByteBuf;

import java.util.Collection;

public interface BatchHandler<T extends MinecraftSession<?>, P> {

    void handle(T session, ByteBuf compressed, Collection<P> packets);
}
