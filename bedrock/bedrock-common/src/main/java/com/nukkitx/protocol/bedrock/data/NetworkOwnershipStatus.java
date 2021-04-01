package com.nukkitx.protocol.bedrock.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class NetworkOwnershipStatus {
    public byte Byte1;
    public long UnsignedLong1;

    public NetworkOwnershipStatus(byte byte1, long UnsignedLong1) {
        setByte1(byte1);
        setUnsignedLong1(UnsignedLong1);
    }
}
