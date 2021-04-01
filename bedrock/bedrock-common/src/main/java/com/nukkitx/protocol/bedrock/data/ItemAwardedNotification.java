package com.nukkitx.protocol.bedrock.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class ItemAwardedNotification {
    public int runtimeEntityId;
    public int amount;
    public UUID catalogItemUuid;

    public ItemAwardedNotification(int int1, int int2, UUID Uuid1) {
        runtimeEntityId = int1;
        amount = int2;
        catalogItemUuid = Uuid1;

    }
}
