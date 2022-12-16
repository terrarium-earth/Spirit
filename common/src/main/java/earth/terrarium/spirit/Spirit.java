package earth.terrarium.spirit;

import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class Spirit {
    public static final String MODID = "spirit";

    public static final TagKey<EntityType<?>> BLACKLISTED_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MODID, "soul_cage_blacklisted"));
    public static final TagKey<EntityType<?>> REVITALIZER_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("vitalize", "revitalizer_blacklist"));
    public static final TagKey<EntityType<?>> COLLECT_BLACKLISTED_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MODID, "collect_blacklisted"));

    public static final TagKey<EntityType<?>> UNCOMMON = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Spirit.MODID, "rarity/uncommon"));
    public static final TagKey<EntityType<?>> RARE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Spirit.MODID, "rarity/rare"));
    public static final TagKey<EntityType<?>> EPIC = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Spirit.MODID, "rarity/epic"));
    public static final TagKey<EntityType<?>> LEGENDARY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Spirit.MODID, "rarity/legendary"));

    public static final int SOUL_COLOR = 0xFF00fffb;

    public static void init() {
        SpiritItems.init();
    }
}