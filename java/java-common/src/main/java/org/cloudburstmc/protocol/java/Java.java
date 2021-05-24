package org.cloudburstmc.protocol.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nukkitx.protocol.MinecraftInterface;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.cloudburstmc.protocol.java.data.profile.GameProfile;
import org.cloudburstmc.protocol.java.data.profile.property.PropertyMap;
import org.cloudburstmc.protocol.java.util.json.GameProfileSerializer;
import org.cloudburstmc.protocol.java.util.json.PropertyMapSerializer;
import org.cloudburstmc.protocol.java.util.json.UUIDTypeAdapter;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public abstract class Java implements MinecraftInterface {
    public static final Gson GSON = GsonComponentSerializer.gson()
            .populator()
            .apply(new GsonBuilder()
                    .registerTypeAdapter(GameProfile.class, new GameProfileSerializer())
                    .registerTypeAdapter(PropertyMap.class, new PropertyMapSerializer())
                    .registerTypeAdapter(UUID.class, new UUIDTypeAdapter())
            )
            .setPrettyPrinting()
            .create();

    protected final InetSocketAddress bindAddress;
    final EventLoopGroup eventLoopGroup;
    private final Bootstrap bootstrap;
    protected boolean handleLogin = true;

    Java(InetSocketAddress bindAddress, EventLoopGroup eventLoopGroup) {
        this.bindAddress = bindAddress;
        this.bootstrap = new Bootstrap();
        this.eventLoopGroup = eventLoopGroup;
        eventLoopGroup.scheduleAtFixedRate(this::onTick, 50, 50, TimeUnit.MILLISECONDS);
    }

    protected abstract void onTick();

    public InetSocketAddress getBindAddress() {
        return this.bindAddress;
    }

    public Bootstrap getBootstrap() {
        return this.bootstrap;
    }

    public abstract void close();
}
