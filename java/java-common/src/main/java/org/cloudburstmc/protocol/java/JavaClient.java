package org.cloudburstmc.protocol.java;

import com.nukkitx.network.util.Bootstraps;
import com.nukkitx.network.util.EventLoops;
import com.nukkitx.network.util.Preconditions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Getter;
import lombok.Setter;
import org.cloudburstmc.protocol.java.handler.JavaStatusPacketHandler;
import org.cloudburstmc.protocol.java.pipeline.PacketDecoder;
import org.cloudburstmc.protocol.java.pipeline.PacketEncoder;
import org.cloudburstmc.protocol.java.pipeline.VarInt21FrameDecoder;
import org.cloudburstmc.protocol.java.pipeline.VarInt21LengthFieldPrepender;
import org.cloudburstmc.protocol.java.packet.State;
import org.cloudburstmc.protocol.java.packet.handshake.HandshakingPacket;
import org.cloudburstmc.protocol.java.packet.status.StatusRequestPacket;
import org.cloudburstmc.protocol.java.packet.status.StatusResponsePacket;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Getter
public class JavaClient extends Java {
    private InetSocketAddress remoteAddress;
    private JavaClientSession session;
    private int timeout;
    private boolean closed = false;
    private boolean connected = false;

    @Setter private JavaEventHandler<JavaClientSession> handler;

    private Bootstrap bootstrap;

    public JavaClient() {
        this(null, EventLoops.commonGroup());
    }

    public JavaClient(InetSocketAddress bindAddress) {
        this(bindAddress, EventLoops.commonGroup());
    }

    public JavaClient(EventLoopGroup eventLoopGroup) {
        this(null, eventLoopGroup);
    }

    public JavaClient(InetSocketAddress bindAddress, EventLoopGroup eventLoopGroup) {
        this(bindAddress, eventLoopGroup, 30_000);
    }

    public JavaClient(EventLoopGroup eventLoopGroup, int timeout) {
        this(null, eventLoopGroup, timeout);
    }

    public JavaClient(@Nullable InetSocketAddress bindAddress, EventLoopGroup eventLoopGroup, int timeout) {
        super(bindAddress, eventLoopGroup);

        this.timeout = timeout;
    }

    @Override
    public InetSocketAddress getBindAddress() {
        if (this.bindAddress == null) {
            return (InetSocketAddress) this.session.getChannel().localAddress();
        }
        return super.getBindAddress();
    }

    @Override
    protected void onTick() {
        if (this.session.isConnected()) {
            this.session.tick();
        }
    }

    @Override
    public CompletableFuture<Void> bind() {
        Preconditions.checkNotNull(this.eventLoopGroup, "Event loop group was null");
        this.bootstrap = new Bootstrap()
                .channel(EventLoops.getChannelType().getSocketChannel())
                .group(this.eventLoopGroup)
                .localAddress(this.bindAddress)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.timeout)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    public void initChannel(Channel channel) throws Exception {
                        JavaClientSession session = new JavaClientSession(JavaClient.this, (InetSocketAddress) channel.localAddress(), channel, channel.eventLoop());
                        channel.pipeline()
                                .addLast("timeout", new ReadTimeoutHandler(JavaClient.this.timeout))
                                .addLast("splitter", new VarInt21FrameDecoder())
                                .addLast("prepender", new VarInt21LengthFieldPrepender())
                                .addLast("encoder", new PacketEncoder(session))
                                .addLast("decoder", new PacketDecoder(session, JavaPacketType.Direction.CLIENTBOUND))
                                .addLast("session", session);
                        JavaClient.this.session = session;
                        JavaClient.this.handler.onSessionCreation(session);
                    }
                });
        if (this.bindAddress != null) {
            this.bootstrap.localAddress(this.bindAddress);
        }
        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<JavaPong> ping(InetSocketAddress remoteAddress) {
        return this.ping(remoteAddress, response -> this.disconnect(null));
    }

    public CompletableFuture<JavaPong> ping(InetSocketAddress remoteAddress, Consumer<StatusResponsePacket> response) {
        Preconditions.checkState(!this.connected, "Client is already connected!");
        CompletableFuture<Void> connectFuture = connectInternal(remoteAddress);
        CompletableFuture<JavaPong> pongFuture = new CompletableFuture<>();
        connectFuture.whenComplete(((aVoid, throwable) -> {
            HandshakingPacket handshakingPacket = new HandshakingPacket();
            handshakingPacket.setProtocolVersion(this.session.getPacketCodec().getProtocolVersion());
            handshakingPacket.setNextState(State.STATUS);
            handshakingPacket.setAddress(this.getBindAddress().getHostName());
            handshakingPacket.setPort(this.getBindAddress().getPort());
            this.session.sendPacket(handshakingPacket);
            this.session.setProtocolState(State.STATUS);
            this.session.sendPacket(new StatusRequestPacket());
            this.session.setPacketHandler(new JavaStatusPacketHandler() {
                @Override
                public boolean handle(StatusResponsePacket packet) {
                    response.accept(packet);
                    pongFuture.complete(packet.getResponse());
                    return true;
                }
            });
        }));
        return pongFuture;
    }

    public CompletableFuture<Void> connect(InetSocketAddress remoteAddress) {
        Preconditions.checkState(!this.connected, "Client is already connected!");
        CompletableFuture<Void> connectFuture = connectInternal(remoteAddress);
        if (this.handleLogin) {
            connectFuture.whenComplete(((aVoid, throwable) -> {
                this.session.handleLogin(remoteAddress);
            }));
        }
        return connectFuture;
    }

    private CompletableFuture<Void> connectInternal(InetSocketAddress remoteAddress) {
        Preconditions.checkNotNull(this.bootstrap, "Client has not been created yet!");
        this.remoteAddress = remoteAddress;
        return Bootstraps.allOf(this.bootstrap.connect(this.remoteAddress).syncUninterruptibly()).whenComplete((aVoid, throwable) -> this.connected = true);
    }

    public void disconnect(String reason) {
        this.session.disconnect(reason);
        this.session = null;
        this.connected = false;
    }

    @Override
    public void close() {
        this.close("disconnect.disconnected");
    }

    public void close(String reason) {
        this.session.disconnect(reason);
        this.closed = true;
    }
}
