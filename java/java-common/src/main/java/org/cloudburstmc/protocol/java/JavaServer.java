package org.cloudburstmc.protocol.java;

import com.nukkitx.network.util.Bootstraps;
import com.nukkitx.network.util.EventLoops;
import com.nukkitx.network.util.Preconditions;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.Setter;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.pipeline.PacketDecoder;
import org.cloudburstmc.protocol.java.pipeline.PacketEncoder;
import org.cloudburstmc.protocol.java.pipeline.VarInt21FrameDecoder;
import org.cloudburstmc.protocol.java.pipeline.VarInt21LengthFieldPrepender;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Getter
@Setter
public class JavaServer extends Java {
    final Set<JavaServerSession> sessions = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private JavaEventHandler<JavaServerSession> handler;
    private boolean closed = false;
    private boolean onlineMode = true;
    private boolean handleKeepalive = true;

    /**
     * Comparable to the handleLogin option, but then for the 'status' stage.
     * This handles the 'status' stage for you and sends the given pong to the client.
     */
    protected Function<JavaServerSession, JavaPong> pong;

    public JavaServer(InetSocketAddress bindAddress) {
        super(bindAddress, EventLoops.commonGroup());
    }

    public JavaServer(InetSocketAddress bindAddress, EventLoopGroup eventLoopGroup) {
        super(bindAddress, eventLoopGroup);
    }

    @Override
    public CompletableFuture<Void> bind() {
        Preconditions.checkNotNull(this.eventLoopGroup, "Event loop group was null");
        ChannelFuture future = new ServerBootstrap()
                .channel(EventLoops.getChannelType().getServerSocketChannel())
                .group(this.eventLoopGroup)
                .localAddress(this.getBindAddress())
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<Channel>() {
            @Override
            public void initChannel(Channel channel) throws Exception {
                JavaServerSession session = new JavaServerSession(JavaServer.this, (InetSocketAddress) channel.remoteAddress(), channel, channel.eventLoop());
                channel.pipeline()
                        .addLast("timeout", new ReadTimeoutHandler(30))
                        .addLast("splitter", new VarInt21FrameDecoder())
                        .addLast("prepender", new VarInt21LengthFieldPrepender())
                        .addLast("encoder", new PacketEncoder(session))
                        .addLast("decoder", new PacketDecoder(session, JavaPacketType.Direction.SERVERBOUND))
                        .addLast("session", session);
                JavaServer.this.sessions.add(session);
                JavaServer.this.handler.onSessionCreation(session);
            }
        }).bind().syncUninterruptibly();
        return Bootstraps.allOf(future);
    }

    @Override
    protected void onTick() {
        Iterator<JavaServerSession> iterator = this.sessions.iterator();
        while (iterator.hasNext()) {
            JavaServerSession session = iterator.next();
            if (session.isConnected()) {
                session.tick();
                return;
            }
            iterator.remove();
        }
    }

    @Override
    public void close() {
        this.close("disconnect.disconnected");
    }

    public void close(String reason) {
        for (JavaServerSession session : this.sessions) {
            session.disconnect(reason);
        }
        this.closed = true;
    }
}
