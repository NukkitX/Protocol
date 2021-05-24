package org.cloudburstmc.protocol.java.v754.serializer.play.clientbound;

import com.nukkitx.network.VarInts;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.cloudburstmc.protocol.java.JavaPacketHelper;
import org.cloudburstmc.protocol.java.JavaPacketSerializer;
import org.cloudburstmc.protocol.java.data.statistic.Statistic;
import org.cloudburstmc.protocol.java.data.statistic.StatisticCategory;
import org.cloudburstmc.protocol.java.data.statistic.StatisticType;
import org.cloudburstmc.protocol.java.packet.play.clientbound.AwardStatsPacket;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AwardStatsSerializer_v754 implements JavaPacketSerializer<AwardStatsPacket> {
    public static final AwardStatsSerializer_v754 INSTANCE = new AwardStatsSerializer_v754();

    @Override
    public void serialize(ByteBuf buffer, JavaPacketHelper helper, AwardStatsPacket packet) {
        helper.writeArray(buffer, packet.getStatistics(), this::writeStatistic);
    }

    @Override
    public void deserialize(ByteBuf buffer, JavaPacketHelper helper, AwardStatsPacket packet) {
        helper.readArray(buffer, packet.getStatistics(), this::readStatistic);
    }

    private Statistic readStatistic(ByteBuf buffer, JavaPacketHelper helper) {
        return new Statistic(StatisticCategory.getById(VarInts.readUnsignedInt(buffer)),
                StatisticType.getById(VarInts.readUnsignedInt(buffer)),
                VarInts.readUnsignedInt(buffer)
        );
    }

    private void writeStatistic(ByteBuf buffer, Statistic statistic) {
        VarInts.writeUnsignedInt(buffer, statistic.getCategory().ordinal());
        VarInts.writeUnsignedInt(buffer, statistic.getStatistic().ordinal());
        VarInts.writeUnsignedInt(buffer, statistic.getValue());
    }
}
