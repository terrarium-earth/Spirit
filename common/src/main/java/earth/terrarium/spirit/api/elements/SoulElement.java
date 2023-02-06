package earth.terrarium.spirit.api.elements;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public interface SoulElement {
    Map<SoulElement, Block> ELEMENTAL_FIRES = new HashMap<>();

    int getColor();
    Component getName();
    TagKey<EntityType<?>> getEntityType();
    TagKey<Fluid> getFluid();
}
