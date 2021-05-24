package org.cloudburstmc.protocol.java;

import com.nukkitx.protocol.MinecraftSession;
import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.cloudburstmc.protocol.java.auth.MojangAuthHandler;
import org.cloudburstmc.protocol.java.auth.SessionsHandler;
import org.cloudburstmc.protocol.java.data.auth.AuthData;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;
import org.cloudburstmc.protocol.java.data.profile.property.PropertyMap;
import org.cloudburstmc.protocol.java.handler.JavaLoginPacketHandler;
import org.cloudburstmc.protocol.java.handler.JavaPacketHandler;
import org.cloudburstmc.protocol.java.packet.State;
import org.cloudburstmc.protocol.java.packet.handshake.HandshakingPacket;
import org.cloudburstmc.protocol.java.packet.login.EncryptionRequestPacket;
import org.cloudburstmc.protocol.java.packet.login.EncryptionResponsePacket;
import org.cloudburstmc.protocol.java.packet.login.LoginStartPacket;
import org.cloudburstmc.protocol.java.packet.login.LoginSuccessPacket;
import org.cloudburstmc.protocol.java.packet.login.SetCompressionPacket;
import org.cloudburstmc.protocol.java.util.EncryptionUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public class JavaClientSession extends JavaSession implements MinecraftSession<JavaPacket<?>> {
    private JavaClient client;
    protected boolean loggedIn;
    protected PropertyMap propertyMap = new PropertyMap();
    protected List<GameProfile> profiles = new ObjectArrayList<>();

    private AuthData authData;

    JavaClientSession(JavaClient client, InetSocketAddress address, Channel channel, EventLoop eventLoop) {
        super(address, channel, eventLoop);

        this.client = client;
    }

    protected void handleLogin(InetSocketAddress remoteAddress) {
        HandshakingPacket handshakingPacket = new HandshakingPacket();
        handshakingPacket.setProtocolVersion(this.packetCodec.getProtocolVersion());
        handshakingPacket.setNextState(State.LOGIN);
        handshakingPacket.setAddress(remoteAddress.getHostName());
        handshakingPacket.setPort(remoteAddress.getPort());
        this.sendPacket(handshakingPacket);
        this.setProtocolState(State.LOGIN);
        createLoginStart().whenComplete((loginStartPacket, ex) -> {
            try {
                this.sendPacket(loginStartPacket);
                this.setPacketHandler(new LoginPacketHandler(this, this.authData, this.getPacketHandler()));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private CompletableFuture<LoginStartPacket> createLoginStart() {
        CompletableFuture<LoginStartPacket> loginStartFuture = new CompletableFuture<>();
        LoginStartPacket loginStartPacket = new LoginStartPacket();
        // Offline auth
        if (this.authData.getPassword() == null && this.authData.getAccessToken() == null && this.authData.getClientToken() == null) {
            loginStartPacket.setProfile(this.profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + this.authData.getUsername()).getBytes(StandardCharsets.UTF_8)), this.authData.getUsername()));
            return CompletableFuture.completedFuture(loginStartPacket);
        }
        // TODO: MSA
        MojangAuthHandler.login(this, this.authData).whenComplete((response, ex) -> {
            loginStartPacket.setProfile(this.profile);
            loginStartFuture.complete(loginStartPacket);
        });
        return loginStartFuture;
    }

    @AllArgsConstructor
    private static final class LoginPacketHandler implements JavaLoginPacketHandler {
        private final JavaClientSession session;
        private final AuthData authData;
        private final JavaPacketHandler packetHandler;

        @Override
        public boolean handle(EncryptionRequestPacket packet) {
            SecretKey secretKey;
            try {
                KeyGenerator gen = KeyGenerator.getInstance("AES");
                gen.init(128);
                secretKey = gen.generateKey();
            } catch(NoSuchAlgorithmException ex) {
                throw new IllegalStateException("Failed to generate shared key:", ex);
            }
            String serverId = EncryptionUtils.createServerId(packet.getPublicKey(), secretKey);
            SessionsHandler.joinServer(this.authData.getAccessToken(), this.session.profile, serverId).whenComplete((response, ex) -> {
                EncryptionResponsePacket responsePacket = new EncryptionResponsePacket();
                responsePacket.setSharedSecret(EncryptionUtils.getSharedKey(packet.getPublicKey(), secretKey));
                responsePacket.setVerifyToken(EncryptionUtils.getVerifyToken(packet.getPublicKey(), packet.getVerifyToken()));
                this.session.sendPacket(responsePacket);
                this.session.enableEncryption(secretKey);
            });
            return true;
        }

        @Override
        public boolean handle(SetCompressionPacket packet) {
            this.session.setCompressionThreshold(packet.getCompressionThreshold());
            return true;
        }

        @Override
        public boolean handle(LoginSuccessPacket packet) {
            this.session.setProtocolState(State.PLAY);
            this.session.setPacketHandler(this.packetHandler);
            this.session.client.getHandler().onLogin(this.session);

            // No need to keep storing these
            this.authData.setClientToken(null);
            this.authData.setAccessToken(null);
            return true;
        }
    }
}
