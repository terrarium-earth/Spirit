package earth.terrarium.spirit.api.elements;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public interface SoulElement {
    Map<SoulElement, Block> ELEMENTAL_FIRES = new HashMap<>();

    int getColor();
    Component getName();
}
