package org.cloudburstmc.protocol.java;

import com.nukkitx.protocol.exception.PacketSerializeException;
import com.nukkitx.protocol.util.Int2ObjectBiMap;
import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.cloudburstmc.protocol.java.packet.State;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;

import static com.nukkitx.network.util.Preconditions.checkArgument;
import static com.nukkitx.network.util.Preconditions.checkNotNull;
import static com.nukkitx.network.util.Preconditions.checkState;

@Getter
@Immutable
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class JavaPacketCodec {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(JavaPacketCodec.class);
    private final int protocolVersion;
    private final String minecraftVersion;
    private final JavaPacketHelper helper;
    
    private final EnumMap<State, JavaStateCodec> stateCodecs;

    public JavaStateCodec getCodec(@Nonnull State protocolState) {
        return this.stateCodecs.get(protocolState);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.protocolVersion = this.protocolVersion;
        builder.minecraftVersion = this.minecraftVersion;
        builder.helper = this.helper;
        builder.stateCodecs = this.stateCodecs;
        return builder;
    }
        
    public static class Builder {
        private int protocolVersion = -1;
        private String minecraftVersion = null;
        private JavaPacketHelper helper;
        private EnumMap<State, JavaStateCodec> stateCodecs = new EnumMap<>(State.class);

        public Builder protocolVersion(@Nonnegative int protocolVersion) {
            checkArgument(protocolVersion >= 0, "protocolVersion cannot be negative");
            this.protocolVersion = protocolVersion;
            return this;
        }

        public Builder minecraftVersion(@Nonnull String minecraftVersion) {
            checkNotNull(minecraftVersion, "minecraftVersion");
            checkArgument(!minecraftVersion.isEmpty() && minecraftVersion.split("\\.").length > 2, "Invalid minecraftVersion");
            this.minecraftVersion = minecraftVersion;
            return this;
        }

        public Builder helper(@Nonnull JavaPacketHelper helper) {
            checkNotNull(helper, "helper");
            this.helper = helper;
            return this;
        }

        public Builder codec(@Nonnull State protocolState, @Nonnull JavaStateCodec codec) {
            checkNotNull(this.helper, "helper");
            checkNotNull(protocolState, "protocolState");
            checkNotNull(codec, "codec");
            this.stateCodecs.put(protocolState, codec);
            codec.helper = this.helper;
            return this;
        }

        public JavaPacketCodec build() {
            checkArgument(this.protocolVersion >= 0, "No protocol version defined");
            checkNotNull(this.minecraftVersion, "No Minecraft version defined");
            checkNotNull(this.helper, "helper cannot be null");
            checkState(this.stateCodecs != null && !this.stateCodecs.isEmpty(), "Codecs cannot be empty");

            return new JavaPacketCodec(this.protocolVersion, this.minecraftVersion, this.helper, this.stateCodecs);
        }
    }
    
    @Immutable
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class JavaStateCodec {
        
        private final JavaPacketSerializer<JavaPacket<?>>[] clientboundSerializers;
        private final Int2ObjectBiMap<Class<? extends JavaPacket<?>>> clientboundIdBiMap;
        private final JavaPacketSerializer<JavaPacket<?>>[] serverboundSerializers;
        private final Int2ObjectBiMap<Class<? extends JavaPacket<?>>> serverboundIdBiMap;

        private JavaPacketHelper helper;
        
        public static Builder builder() {
            return new Builder();
        }

        public JavaPacket<?> tryDecode(ByteBuf buf, int id, JavaSession session, JavaPacketType.Direction direction) throws PacketSerializeException {
            JavaPacket<?> packet;
            JavaPacketSerializer<JavaPacket<?>> serializer;
            try {
                if (direction == JavaPacketType.Direction.CLIENTBOUND) {
                    packet = this.clientboundIdBiMap.get(id).getDeclaredConstructor().newInstance();
                    serializer = this.clientboundSerializers[id];
                } else {
                    packet = this.serverboundIdBiMap.get(id).getDeclaredConstructor().newInstance();
                    serializer = this.serverboundSerializers[id];
                }
            } catch (ClassCastException | InstantiationException | IllegalAccessException | ArrayIndexOutOfBoundsException | NoSuchMethodException | InvocationTargetException e) {
                throw new PacketSerializeException("Packet ID " + id + " does not exist", e);
            }

            try {
                serializer.deserialize(buf, this.helper, packet, session);
            } catch (Exception e) {
                throw new PacketSerializeException("Error whilst deserializing " + packet + " during state " + session.getProtocolState(), e);
            }

            if (log.isDebugEnabled() && buf.isReadable()) {
                log.debug(packet.getClass().getSimpleName() + " still has " + buf.readableBytes() + " bytes to read!");
            }
            return packet;
        }

        public void tryEncode(ByteBuf buf, JavaPacket<?> packet, JavaSession session, boolean clientbound) throws PacketSerializeException {
            try {
                JavaPacketSerializer<JavaPacket<?>> serializer;
                int packetId = getId(packet, clientbound);
                serializer = clientbound ? this.clientboundSerializers[packetId] : this.serverboundSerializers[packetId];
                serializer.serialize(buf, this.helper, packet, session);
            } catch (Exception e) {
                throw new PacketSerializeException("Error whilst serializing " + packet, e);
            } finally {
                ReferenceCountUtil.release(packet);
            }
        }

        @SuppressWarnings("unchecked")
        public int getId(JavaPacket<?> packet, boolean clientbound) {
            return getId((Class<? extends JavaPacket<?>>) packet.getClass(), clientbound);
        }

        public int getId(Class<? extends JavaPacket<?>> clazz, boolean clientbound) {
            int id = clientbound ? this.clientboundIdBiMap.get(clazz) : this.serverboundIdBiMap.get(clazz);
            if (id == -1) {
                throw new IllegalArgumentException("Packet ID for " + clazz.getName() + " does not exist.");
            }
            return id;
        }

        @SuppressWarnings("unchecked")
        public Builder toBuilder() {
            Builder builder = new Builder();

            clientboundIdBiMap.forEach((packetClass, id) ->
                    builder.registerClientbound((Class<JavaPacket<?>>) packetClass, this.clientboundSerializers[id], id));
            serverboundIdBiMap.forEach((packetClass, id) ->
                    builder.registerServerbound((Class<JavaPacket<?>>) packetClass, this.serverboundSerializers[id], id));

            return builder;
        }

        @SuppressWarnings("unchecked")
        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Builder {
            private final Int2ObjectMap<JavaPacketSerializer<JavaPacket<?>>> clientboundSeriaizers = new Int2ObjectOpenHashMap<>();
            private final Int2ObjectBiMap<Class<? extends JavaPacket<?>>> clientboundIdBiMap = new Int2ObjectBiMap<>();
            private final Int2ObjectMap<JavaPacketSerializer<JavaPacket<?>>> serverboundSeriaizers = new Int2ObjectOpenHashMap<>();
            private final Int2ObjectBiMap<Class<? extends JavaPacket<?>>> serverboundIdBiMap = new Int2ObjectBiMap<>();

            public <T extends JavaPacket<?>> Builder registerClientbound(Class<T> packetClass, JavaPacketSerializer<T> serializer, @Nonnegative int id) {
                checkArgument(id >= 0, "id cannot be negative");
                checkArgument(!clientboundIdBiMap.containsKey(id), "Packet id already registered");
                checkArgument(!clientboundIdBiMap.containsValue(packetClass), "Packet class already registered");

                clientboundSeriaizers.put(id, (JavaPacketSerializer<JavaPacket<?>>) serializer);
                clientboundIdBiMap.put(id, packetClass);
                return this;
            }

            public void deregisterClientbound(Class<? extends JavaPacket<?>> packetClass) {
                checkNotNull(packetClass, "packetClass");
                if (!this.clientboundIdBiMap.containsValue(packetClass)) return;
                int packetId = this.clientboundIdBiMap.get(packetClass);
                this.clientboundIdBiMap.remove(packetClass);
                this.clientboundSeriaizers.remove(packetId);
            }

            public <T extends JavaPacket<?>> Builder registerServerbound(Class<T> packetClass, JavaPacketSerializer<T> serializer, @Nonnegative int id) {
                checkArgument(id >= 0, "id cannot be negative");
                checkArgument(!serverboundIdBiMap.containsKey(id), "Packet id already registered");
                checkArgument(!serverboundIdBiMap.containsValue(packetClass), "Packet class already registered");

                serverboundSeriaizers.put(id, (JavaPacketSerializer<JavaPacket<?>>) serializer);
                serverboundIdBiMap.put(id, packetClass);
                return this;
            }

            public void deregisterServerbound(Class<? extends JavaPacket<?>> packetClass) {
                checkNotNull(packetClass, "packetClass");
                if (!this.serverboundIdBiMap.containsValue(packetClass)) return;
                int packetId = this.serverboundIdBiMap.get(packetClass);
                this.serverboundIdBiMap.remove(packetClass);
                this.serverboundSeriaizers.remove(packetId);
            }

            public JavaStateCodec build() {
                int largestId = -1;
                for (int id : clientboundSeriaizers.keySet()) {
                    if (id > largestId) {
                        largestId = id;
                    }
                }
                JavaPacketSerializer<JavaPacket<?>>[] clientboundSerializers = new JavaPacketSerializer[largestId + 1];
                for (Int2ObjectMap.Entry<JavaPacketSerializer<JavaPacket<?>>> entry : this.clientboundSeriaizers.int2ObjectEntrySet()) {
                    clientboundSerializers[entry.getIntKey()] = entry.getValue();
                }

                for (int id : serverboundSeriaizers.keySet()) {
                    if (id > largestId) {
                        largestId = id;
                    }
                }

                JavaPacketSerializer<JavaPacket<?>>[] serverboundSerializers = new JavaPacketSerializer[largestId + 1];
                for (Int2ObjectMap.Entry<JavaPacketSerializer<JavaPacket<?>>> entry : this.serverboundSeriaizers.int2ObjectEntrySet()) {
                    serverboundSerializers[entry.getIntKey()] = entry.getValue();
                }
                return new JavaStateCodec(clientboundSerializers, clientboundIdBiMap, serverboundSerializers, serverboundIdBiMap);
            }
        }
    }
}
