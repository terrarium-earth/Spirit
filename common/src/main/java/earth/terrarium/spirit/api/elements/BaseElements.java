package earth.terrarium.spirit.api.elements;

import earth.terrarium.spirit.common.registry.SpiritTags;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;

public enum BaseElements implements SoulElement {
    EMBER(0xff7a16, "elements.spirit.ember", SpiritTags.EMBER, SpiritTags.EMBER_ESSENCE),
    WATER(0x28b3ae, "elements.spirit.water", SpiritTags.WATER, SpiritTags.WATER_ESSENCE),
    EARTH(0x84c83d, "elements.spirit.earth", SpiritTags.EARTH, SpiritTags.EARTH_ESSENCE),
    ENDER(0xda58eb, "elements.spirit.ender", SpiritTags.ENDER, SpiritTags.ENDER_ESSENCE);
    final String name;
    final int color;
    final TagKey<EntityType<?>> entityType;
    final TagKey<Fluid> fluid;

    BaseElements(int color, String name, TagKey<EntityType<?>> entityType, TagKey<Fluid> fluid) {
        this.color = color;
        this.name = name;
        this.entityType = entityType;
        this.fluid = fluid;
    }


    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Component getName() {
        return Component.translatable(name);
    }

    @Override
    public TagKey<EntityType<?>> getEntityType() {
        return entityType;
    }

    @Override
    public TagKey<Fluid> getFluid() {
        return fluid;
    }
}
