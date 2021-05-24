package org.cloudburstmc.protocol.java.packet.play.clientbound;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.key.Key;
import org.cloudburstmc.protocol.java.JavaPacket;
import org.cloudburstmc.protocol.java.handler.JavaPlayPacketHandler;
import org.cloudburstmc.protocol.java.packet.type.JavaPacketType;
import org.cloudburstmc.protocol.java.packet.type.JavaPlayPacketType;

import java.util.List;

@Data
@EqualsAndHashCode(doNotUseGetters = true, callSuper = false)
public class RecipePacket extends JavaPacket<JavaPlayPacketHandler> {
    private State state;
    private final List<Key> recipes = new ObjectArrayList<>();
    private final List<Key> highlightedRecipes = new ObjectArrayList<>();
    private boolean guiOpen;
    private boolean filteringCraftable;
    private boolean furnaceGuiOpen;
    private boolean furnaceFilteringCraftable;
    private boolean blastingFurnaceGuiOpen;
    private boolean blastingFurnaceFilteringCraftable;
    private boolean smokerGuiOpen;
    private boolean smokerFilteringCraftable;

    @Override
    public boolean handle(JavaPlayPacketHandler handler) {
        return handler.handle(this);
    }

    @Override
    public JavaPacketType getPacketType() {
        return JavaPlayPacketType.RECIPE_S2C;
    }

    public enum State {
        INIT,
        ADD,
        REMOVE;
    }
}
