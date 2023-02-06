package earth.terrarium.spirit.common.registry;

import earth.terrarium.spirit.Spirit;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;

public class SpiritTags {
    public static final TagKey<EntityType<?>> EMBER = TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), new ResourceLocation(Spirit.MODID, "ember"));
    public static final TagKey<EntityType<?>> WATER = TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), new ResourceLocation(Spirit.MODID, "water"));
    public static final TagKey<EntityType<?>> EARTH = TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), new ResourceLocation(Spirit.MODID, "earth"));
    public static final TagKey<EntityType<?>> ENDER = TagKey.create(BuiltInRegistries.ENTITY_TYPE.key(), new ResourceLocation(Spirit.MODID, "ender"));

    public static final TagKey<Fluid> EMBER_ESSENCE = TagKey.create(BuiltInRegistries.FLUID.key(), new ResourceLocation(Spirit.MODID, "ember_essence"));
    public static final TagKey<Fluid> WATER_ESSENCE = TagKey.create(BuiltInRegistries.FLUID.key(), new ResourceLocation(Spirit.MODID, "water_essence"));
    public static final TagKey<Fluid> EARTH_ESSENCE = TagKey.create(BuiltInRegistries.FLUID.key(), new ResourceLocation(Spirit.MODID, "earth_essence"));
    public static final TagKey<Fluid> ENDER_ESSENCE = TagKey.create(BuiltInRegistries.FLUID.key(), new ResourceLocation(Spirit.MODID, "ender_essence"));
}
