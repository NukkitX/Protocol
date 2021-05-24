package org.cloudburstmc.protocol.java.data.inventory;

import com.nukkitx.nbt.NbtMap;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ItemStack {
    int id;
    int amount;
    NbtMap nbt;

    public ItemStack(int id) {
        this(id, 1);
    }

    public ItemStack(int id, int amount) {
        this(id, amount, null);
    }
}
