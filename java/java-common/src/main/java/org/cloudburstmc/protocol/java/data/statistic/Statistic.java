package org.cloudburstmc.protocol.java.data.statistic;

import lombok.Value;

@Value
public class Statistic {
    StatisticCategory category;
    StatisticType statistic;
    int value;
}
