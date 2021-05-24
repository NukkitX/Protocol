package org.cloudburstmc.protocol.java.data.entity;

import lombok.Value;
import org.cloudburstmc.protocol.java.data.inventory.ItemStack;

@Value
public class Equipment {
    EquipmentSlot slot;
    ItemStack itemStack;
}
