package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.world.Difficulty;
import org.cloudburstmc.protocol.java.packet.play.clientbound.ChangeDifficultyPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChangeDifficultySerializer_v754 implements JavaPacketSerializer<ChangeDifficultyPacket> {
    public static final ChangeDifficultySerializer_v754 INSTANCE = new ChangeDifficultySerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, ChangeDifficultyPacket packet) {
        buffer.writeByte(packet.getDifficulty().ordinal());
        buffer.writeBoolean(packet.isLocked());
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, ChangeDifficultyPacket packet) {
        packet.setDifficulty(Difficulty.getById(buffer.readUnsignedByte()));
        packet.setLocked(buffer.readBoolean());
    }
}
