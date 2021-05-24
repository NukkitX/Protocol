package org.cloudburstmc.protocol.java;

import com.nukkitx.protocol.MinecraftServerSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import lombok.AllArgsConstructor;
import org.cloudburstmc.protocol.java.auth.SessionsHandler;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;
import org.cloudburstmc.protocol.java.exception.ProfileException;
import org.cloudburstmc.protocol.java.handler.JavaHandshakePacketHandler;
import org.cloudburstmc.protocol.java.handler.JavaLoginPacketHandler;
import org.cloudburstmc.protocol.java.handler.JavaPacketHandler;
import org.cloudburstmc.protocol.java.handler.JavaStatusPacketHandler;
import org.cloudburstmc.protocol.java.packet.State;
import org.cloudburstmc.protocol.java.packet.handshake.HandshakingPacket;
import org.cloudburstmc.protocol.java.packet.login.*;
import org.cloudburstmc.protocol.java.packet.play.KeepAlivePacket;
import org.cloudburstmc.protocol.java.packet.status.PingPacket;
import org.cloudburstmc.protocol.java.packet.status.PongPacket;
import org.cloudburstmc.protocol.java.packet.status.StatusRequestPacket;
import org.cloudburstmc.protocol.java.packet.status.StatusResponsePacket;
import org.cloudburstmc.protocol.java.util.EncryptionUtils;

import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class JavaServerSession extends JavaSession implements MinecraftServerSession<JavaPacket<?>> {
    private final JavaServer server;
    private final byte[] verifyToken = new byte[4];

    JavaServerSession(JavaServer server, InetSocketAddress address, Channel channel, EventLoop eventLoop) {
        super(address, channel, eventLoop);

        this.server = server;
    }

    protected void handleLogin(boolean onlineMode) {
        this.setProtocolState(State.LOGIN);
        this.setPacketHandler(new LoginPacketHandler(this, this.getPacketHandler(), onlineMode));
        if (onlineMode) {
            ThreadLocalRandom.current().nextBytes(this.verifyToken);
        }
    }

    protected void handleStatus(Function<JavaServerSession, JavaPong> response) {
        setProtocolState(State.STATUS);
        setPacketHandler(new StatusPacketHandler(this, response));
    }

    @Override
    public void tick() {
        long ctm;

        if (server.isHandleKeepalive() && getProtocolState() == State.PLAY &&
                (ctm = System.currentTimeMillis()) - lastKeepAlive > 10_000) {
            lastKeepAlive = ctm;

            KeepAlivePacket keepAlive = new KeepAlivePacket();
            keepAlive.setId(lastKeepAlive);
            sendPacket(keepAlive);
        }

        super.tick();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JavaPacket<?> packet) throws Exception {
        if ((this.server.handleLogin || this.server.pong != null) && packet instanceof HandshakingPacket) {
            HandshakingPacket handshakingPacket = (HandshakingPacket) packet;
            if (this.server.handleLogin && handshakingPacket.getNextState() == State.LOGIN) {
                this.handleLogin(this.server.isOnlineMode());
            } else if (this.server.pong != null && handshakingPacket.getNextState() == State.STATUS) {
                handleStatus(this.server.pong);
            }
        }

        super.channelRead0(ctx, packet);
    }

    @AllArgsConstructor
    private static final class LoginPacketHandler implements JavaLoginPacketHandler, JavaHandshakePacketHandler {
        private final JavaServerSession session;
        private final JavaPacketHandler packetHandler;
        private final boolean onlineMode;

        @Override
        public boolean handle(LoginStartPacket packet) {
            this.session.setProfile(packet.getProfile());
            if (!this.onlineMode) {
                LoginSuccessPacket successPacket = new LoginSuccessPacket();
                successPacket.setProfile(new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + packet.getProfile().getName()).getBytes(StandardCharsets.UTF_8)), packet.getProfile().getName()));
                this.session.sendPacket(successPacket);
                this.session.setProtocolState(State.PLAY);
                this.session.setPacketHandler(this.packetHandler);
                this.session.server.getHandler().onLogin(this.session);
                return true;
            }
            EncryptionRequestPacket requestPacket = new EncryptionRequestPacket();
            requestPacket.setPublicKey(EncryptionUtils.KEY_PAIR.getPublic());
            requestPacket.setVerifyToken(this.session.verifyToken);
            this.session.sendPacket(requestPacket);
            return true;
        }

        @Override
        public boolean handle(EncryptionResponsePacket packet) {
            PrivateKey privateKey = EncryptionUtils.KEY_PAIR.getPrivate();
            if (!Arrays.equals(this.session.verifyToken, EncryptionUtils.getVerifyToken(privateKey, packet.getVerifyToken()))) {
                this.session.disconnect("Invalid nonce!");
                return true;
            }
            SecretKey secretKey = EncryptionUtils.getSecret(privateKey, packet.getSharedSecret());
            this.session.enableEncryption(secretKey);
            SessionsHandler.getHasJoinedServer(this.session.getProfile(), EncryptionUtils.createServerId(EncryptionUtils.KEY_PAIR.getPublic(), secretKey)).whenComplete(((response, throwable) -> {
                if (response == null) {
                    throw new CompletionException(new ProfileException("Invalid profile response!"));
                }
                if (response.getId().equals(this.session.profile.getId())) {
                    throw new CompletionException(new ProfileException("Response profile id was not the same as the profile id of the currently active session!"));
                }
                this.session.profile.setId(response.getId());
                this.session.profile.setProperties(response.getProperties());
                if (this.session.getCompressionThreshold() >= 0) {
                    SetCompressionPacket setCompressionPacket = new SetCompressionPacket();
                    setCompressionPacket.setCompressionThreshold(this.session.getCompressionThreshold());
                    this.session.sendPacket(setCompressionPacket);
                    this.session.setCompressionThreshold(this.session.getCompressionThreshold());
                }
                LoginSuccessPacket loginSuccessPacket = new LoginSuccessPacket();
                loginSuccessPacket.setProfile(this.session.profile);
                this.session.sendPacket(loginSuccessPacket);
                this.session.setProtocolState(State.PLAY);
                this.session.setPacketHandler(this.packetHandler);
                this.session.server.getHandler().onLogin(this.session);
            }));
            return true;
        }
    }

    @AllArgsConstructor
    private static final class StatusPacketHandler implements JavaStatusPacketHandler, JavaHandshakePacketHandler {
        private final JavaServerSession session;
        private final Function<JavaServerSession, JavaPong> pong;

        @Override
        public boolean handle(StatusRequestPacket packet) {
            StatusResponsePacket response = new StatusResponsePacket();
            response.setResponse(pong.apply(session));

            session.sendPacket(response);
            return true;
        }

        @Override
        public boolean handle(PingPacket packet) {
            PongPacket pong = new PongPacket();
            pong.setTimestamp(packet.getTimestamp());

            session.sendPacket(pong);
            return true;
        }
    }
}
