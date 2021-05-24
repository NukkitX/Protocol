package org.cloudburstmc.protocol.java.data.entity;

import lombok.Value;

@Value
public class EntityData<T> {
    int id;
    EntityDataType<T> type;
    T value;
}
