package me.codexadrian.spirit.compat.rei;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.compat.jei.ingredients.EntityIngredient;
import me.codexadrian.spirit.compat.rei.categories.PedestalRecipeCategory;
import me.codexadrian.spirit.compat.rei.categories.SoulCageCategory;
import me.codexadrian.spirit.compat.rei.categories.SoulEngulfingCategory;
import me.codexadrian.spirit.compat.rei.displays.PedestalDisplay;
import me.codexadrian.spirit.compat.rei.displays.SoulEngulfingDisplay;
import me.codexadrian.spirit.compat.rei.displays.TierDisplay;
import me.codexadrian.spirit.compat.rei.ingredients.EntityIngredientDefinition;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.recipe.PedestalRecipe;
import me.codexadrian.spirit.recipe.SoulEngulfingRecipe;
import me.codexadrian.spirit.registry.SpiritBlocks;
import me.codexadrian.spirit.registry.SpiritMisc;
import me.codexadrian.spirit.registry.SpiritRecipes;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class SpiritPlugin implements REIClientPlugin {
    public static final EntryType<EntityIngredient> ENTITY_INGREDIENT = EntryType.deferred(new ResourceLocation(Spirit.MODID, "entity_ingredient"));

    @Override
    public void registerEntryTypes(EntryTypeRegistry registry) {
        registry.register(ENTITY_INGREDIENT, new EntityIngredientDefinition());
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new PedestalRecipeCategory());
        registry.add(new SoulCageCategory());
        registry.add(new SoulEngulfingCategory());

        registry.addWorkstations(PedestalRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.SOUL_PEDESTAL.get().asItem().getDefaultInstance()));
        registry.addWorkstations(PedestalRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.PEDESTAL.get().asItem().getDefaultInstance()));
        registry.addWorkstations(SoulCageCategory.RECIPE, EntryStacks.of(SpiritBlocks.SOUL_CAGE.get().asItem().getDefaultInstance()));
        registry.addWorkstations(SoulEngulfingCategory.RECIPE, EntryStacks.of(Blocks.SOUL_SAND.asItem().getDefaultInstance()));
        registry.addWorkstations(SoulEngulfingCategory.RECIPE, EntryStacks.of(Items.FLINT_AND_STEEL.asItem().getDefaultInstance()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(PedestalRecipe.class, SpiritRecipes.getSoulTransmutationRecipe().get(), PedestalDisplay::new);
        registry.registerRecipeFiller(Tier.class, SpiritRecipes.getTierRecipe().get(), TierDisplay::new);
        registry.registerRecipeFiller(SoulEngulfingRecipe.class, SpiritRecipes.getSoulEngulfingRecipe().get(), SoulEngulfingDisplay::new);
    }
}
