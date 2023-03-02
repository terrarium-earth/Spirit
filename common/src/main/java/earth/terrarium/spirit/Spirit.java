package earth.terrarium.spirit;

import earth.terrarium.spirit.api.event.SoulGatherEvent;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.common.registry.SpiritItems;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Spirit {
    public static final String MODID = "spirit";
    public static final String MOD_NAME = "Spirit";

    public static final TagKey<EntityType<?>> BLACKLISTED_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MODID, "soul_cage_blacklisted"));
    public static final TagKey<EntityType<?>> REVITALIZER_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("vitalize", "revitalizer_blacklist"));
    public static final TagKey<EntityType<?>> COLLECT_BLACKLISTED_TAG = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MODID, "collect_blacklisted"));

    public static final TagKey<EntityType<?>> UNCOMMON = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Spirit.MODID, "rarity/uncommon"));
    public static final TagKey<EntityType<?>> RARE = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Spirit.MODID, "rarity/rare"));
    public static final TagKey<EntityType<?>> EPIC = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Spirit.MODID, "rarity/epic"));
    public static final TagKey<EntityType<?>> LEGENDARY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Spirit.MODID, "rarity/legendary"));

    public static final TagKey<EntityType<?>> SOUL_CAGE_CONDITIONS_IGNORED = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Spirit.MODID, "soul_cage_conditions_ignored"));

    public static final TagKey<Item> SOUL_FIRE_IMMUNE = TagKey.create(Registries.ITEM, new ResourceLocation(MODID, "soul_fire_immune"));
    public static final TagKey<Item> SOUL_FIRE_REPAIRABLE = TagKey.create(Registries.ITEM, new ResourceLocation(MODID, "soul_fire_repairable"));

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final int SOUL_COLOR = 0xFF00fffb;

    public static void init() {
        SpiritItems.ITEMS.init();
        SpiritBlocks.BLOCKS.init();
        SpiritBlockEntities.BLOCK_ENTITIES.init();
        SpiritRecipes.RECIPE_TYPES.init();
        SpiritRecipes.RECIPE_SERIALIZERS.init();

        SoulGatherEvent.register(new ResourceLocation(MODID, "scythe"), (victim, player, amount) -> {
            if (player.getMainHandItem().getItem() == SpiritItems.SCYTHE.get()) {
                return amount + 1;
            }
            return amount;
        });
    }
}