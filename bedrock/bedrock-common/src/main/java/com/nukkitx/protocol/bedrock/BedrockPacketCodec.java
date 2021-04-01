package com.nukkitx.protocol.bedrock;

import com.nukkitx.protocol.bedrock.exception.PacketSerializeException;
import com.nukkitx.protocol.bedrock.packet.UnknownPacket;
import com.nukkitx.protocol.util.Int2ObjectBiMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.nukkitx.network.util.Preconditions.checkArgument;
import static com.nukkitx.network.util.Preconditions.checkNotNull;

@Immutable
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class BedrockPacketCodec {
    private static final InternalLogger log = InternalLoggerFactory.getInstance(BedrockPacketCodec.class);

    @Getter
    private final int protocolVersion;
    @Getter
    private final String minecraftVersion;
    private final BedrockPacketSerializer<BedrockPacket>[] serializers;
    private final Int2ObjectBiMap<Class<? extends BedrockPacket>> idBiMap;
    private final BedrockPacketHelper helper;
    @Getter
    private final int raknetProtocolVersion;

    public static Builder builder() {
        return new Builder();
    }

    public BedrockPacket tryDecode(ByteBuf buf, int id, BedrockSession session) throws PacketSerializeException {
        BedrockPacket packet;
        BedrockPacketSerializer<BedrockPacket> serializer;
        try {
            packet = idBiMap.get(id).newInstance();
            if (packet instanceof UnknownPacket) {
                //noinspection unchecked
                serializer = (BedrockPacketSerializer<BedrockPacket>) packet;
            } else {
                serializer = serializers[id];
            }
        } catch (ClassCastException | InstantiationException | IllegalAccessException | ArrayIndexOutOfBoundsException e) {
            throw new PacketSerializeException("Packet ID " + id + " does not exist", e);
        }

        try {
            serializer.deserialize(buf, this.helper, packet, session);
        } catch (Exception e) {
            throw new PacketSerializeException("Error whilst deserializing " + packet, e);
        }

        if (log.isDebugEnabled() && buf.isReadable()) {
            log.debug(packet.getClass().getSimpleName() + " still has " + buf.readableBytes() + " bytes to read!");
            log.debug("Contents: " + ByteBufUtil.prettyHexDump(buf));
        }
        return packet;
    }

    @SuppressWarnings("unchecked")
    public void tryEncode(ByteBuf buf, BedrockPacket packet, BedrockSession session) throws PacketSerializeException {
        try {
            BedrockPacketSerializer<BedrockPacket> serializer;
            if (packet instanceof UnknownPacket) {
                serializer = (BedrockPacketSerializer<BedrockPacket>) packet;
            } else {
                int packetId = getId(packet.getClass());
                serializer = serializers[packetId];
            }
            serializer.serialize(buf, this.helper, packet, session);
        } catch (Exception e) {
            throw new PacketSerializeException("Error whilst serializing " + packet, e);
        } finally {
            ReferenceCountUtil.release(packet);
        }
    }

    public int getId(BedrockPacket packet) {
        if (packet instanceof UnknownPacket) {
            return packet.getPacketId();
        }
        return getId(packet.getClass());
    }

    public int getId(Class<? extends BedrockPacket> clazz) {
        int id = idBiMap.get(clazz);
        if (id == -1) {
            throw new IllegalArgumentException("Packet ID for " + clazz.getName() + " does not exist.");
        }
        return id;
    }

    @SuppressWarnings("unchecked")
    public Builder toBuilder() {
        Builder builder = new Builder();

        idBiMap.forEach((packetClass, id) ->
                builder.registerPacket((Class<BedrockPacket>) packetClass, this.serializers[id], id));

        builder.protocolVersion = this.protocolVersion;
        builder.raknetProtocolVersion = this.raknetProtocolVersion;
        builder.minecraftVersion = this.minecraftVersion;
        builder.helper = this.helper;

        return builder;
    }

    @SuppressWarnings("unchecked")
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private final Int2ObjectMap<BedrockPacketSerializer<BedrockPacket>> serializers = new Int2ObjectOpenHashMap<>();
        private final Int2ObjectBiMap<Class<? extends BedrockPacket>> idBiMap = new Int2ObjectBiMap<>(UnknownPacket.class);
        private int protocolVersion = -1;
        private int raknetProtocolVersion = 10;
        private String minecraftVersion = null;
        private BedrockPacketHelper helper = null;

        public <T extends BedrockPacket> Builder registerPacket(Class<T> packetClass, BedrockPacketSerializer<T> serializer, @Nonnegative int id) {
            checkArgument(id >= 0, "id cannot be negative");
            checkArgument(!idBiMap.containsKey(id), "Packet id already registered");
            checkArgument(!idBiMap.containsValue(packetClass), "Packet class already registered");

            serializers.put(id, (BedrockPacketSerializer<BedrockPacket>) serializer);
            idBiMap.put(id, packetClass);
            return this;
        }

        public void deregisterPacket(Class<? extends BedrockPacket> packetClass) {
            checkNotNull(packetClass, "packetClass");
            if (!this.idBiMap.containsValue(packetClass)) return;
            int packetId = this.idBiMap.get(packetClass);
            this.idBiMap.remove(packetClass);
            this.serializers.remove(packetId);
        }

        public Builder protocolVersion(@Nonnegative int protocolVersion) {
            checkArgument(protocolVersion >= 0, "protocolVersion cannot be negative");
            this.protocolVersion = protocolVersion;
            return this;
        }

        public Builder raknetProtocolVersion(@Nonnegative int version) {
            checkArgument(version >= 0, "raknetProtocolVersion cannot be negative");
            this.raknetProtocolVersion = version;
            return this;
        }

        public Builder minecraftVersion(@Nonnull String minecraftVersion) {
            checkNotNull(minecraftVersion, "minecraftVersion");
            checkArgument(!minecraftVersion.isEmpty() && minecraftVersion.split("\\.").length > 2, "Invalid minecraftVersion");
            this.minecraftVersion = minecraftVersion;
            return this;
        }

        public Builder helper(@Nonnull BedrockPacketHelper helper) {
            checkNotNull(helper, "helper");
            this.helper = helper;
            return this;
        }

        public BedrockPacketCodec build() {
            checkArgument(protocolVersion >= 0, "No protocol version defined");
            checkNotNull(minecraftVersion, "No Minecraft version defined");
            checkNotNull(helper, "helper cannot be null");
            int largestId = -1;
            for (int id : serializers.keySet()) {
                if (id > largestId) {
                    largestId = id;
                }
            }
            checkArgument(largestId > -1, "Must have at least one packet registered");
            BedrockPacketSerializer<BedrockPacket>[] serializers = new BedrockPacketSerializer[largestId + 1];

            for (Int2ObjectMap.Entry<BedrockPacketSerializer<BedrockPacket>> entry : this.serializers.int2ObjectEntrySet()) {
                serializers[entry.getIntKey()] = entry.getValue();
            }
            return new BedrockPacketCodec(protocolVersion, minecraftVersion, serializers, idBiMap, helper, raknetProtocolVersion);
        }
    }
}
