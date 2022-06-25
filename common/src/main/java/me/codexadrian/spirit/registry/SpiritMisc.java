package me.codexadrian.spirit.registry;

import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.enchantments.SoulReaperEnchantment;
import me.codexadrian.spirit.entity.CrudeSoulEntity;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import me.codexadrian.spirit.recipe.PedestalRecipe;
import me.codexadrian.spirit.recipe.SoulEngulfingRecipe;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.function.Supplier;

import static me.codexadrian.spirit.platform.Services.REGISTRY;

public class SpiritMisc {
    public static final Supplier<EntityType<SoulArrowEntity>> SOUL_ARROW_ENTITY = REGISTRY.registerEntity("soul_arrow", SoulArrowEntity::new, MobCategory.MISC, 0.5F, 0.5F);

    public static final Supplier<EntityType<CrudeSoulEntity>> SOUL_ENTITY = REGISTRY.registerEntity("soul", CrudeSoulEntity::new, MobCategory.MISC, 0.5F, 0.5F);

    public static final Supplier<RecipeSerializer<SoulEngulfingRecipe>> SOUL_ENGULFING_SERIALIZER = REGISTRY.registerRecipeSerializer("soul_engulfing", SpiritRecipes.getSoulEngulfingRecipe().get(), SoulEngulfingRecipe::codec);

    public static final Supplier<RecipeSerializer<Tier>> TIER_SERIALIZER = REGISTRY.registerRecipeSerializer("soul_cage_tier", SpiritRecipes.getTierRecipe().get(), Tier::codec);

    public static final Supplier<RecipeSerializer<MobTraitData>> MOB_TRAIT_SERIALIZER = REGISTRY.registerRecipeSerializer("mob_trait", SpiritRecipes.getMobTraitRecipe().get(), MobTraitData::codec);

    public static final Supplier<RecipeSerializer<PedestalRecipe>> SOUL_TRANSMUTATION_SERIALIZER = REGISTRY.registerRecipeSerializer("soul_transmutation", SpiritRecipes.getSoulTransmutationRecipe().get(), PedestalRecipe::codec);

    public static final Supplier<Enchantment> SOUL_REAPER_ENCHANTMENT = REGISTRY.registerEnchantment("soul_reaper", SoulReaperEnchantment::new);

    public static void registerAll() {
    }

}
