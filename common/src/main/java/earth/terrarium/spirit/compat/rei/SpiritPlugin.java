package earth.terrarium.spirit.compat.rei;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.recipes.SoulEngulfingRecipe;
import earth.terrarium.spirit.common.recipes.SummoningRecipe;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.rei.categories.SummoningRecipeCategory;
import earth.terrarium.spirit.compat.rei.categories.SoulEngulfingCategory;
import earth.terrarium.spirit.compat.rei.categories.TransmutationRecipeCategory;
import earth.terrarium.spirit.compat.rei.displays.SummoningDisplay;
import earth.terrarium.spirit.compat.rei.displays.SoulEngulfingDisplay;
import earth.terrarium.spirit.compat.rei.displays.TransmutationDisplay;
import earth.terrarium.spirit.compat.rei.ingredients.EntityIngredientDefinition;
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
        registry.add(new SummoningRecipeCategory());
        registry.add(new SoulEngulfingCategory());
        registry.add(new TransmutationRecipeCategory());

        registry.addWorkstations(SummoningRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.PEDESTAL.get().asItem().getDefaultInstance()));
        registry.addWorkstations(SummoningRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.SOUL_BASIN.get().asItem().getDefaultInstance()));
        registry.addWorkstations(SummoningRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.SUMMONING_PEDESTAL.get().asItem().getDefaultInstance()));

        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.PEDESTAL.get().asItem().getDefaultInstance()));
        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.SOUL_BASIN.get().asItem().getDefaultInstance()));
        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.TRANSMUTATION_PEDESTAL.get().asItem().getDefaultInstance()));

        registry.addWorkstations(SoulEngulfingCategory.RECIPE, EntryStacks.of(Blocks.SOUL_SAND.asItem().getDefaultInstance()));
        registry.addWorkstations(SoulEngulfingCategory.RECIPE, EntryStacks.of(Items.FLINT_AND_STEEL.asItem().getDefaultInstance()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(SummoningRecipe.class, SpiritRecipes.SUMMONING.get(), SummoningDisplay::new);
        registry.registerRecipeFiller(TransmutationRecipe.class, SpiritRecipes.TRANSMUTATION.get(), TransmutationDisplay::new);
        registry.registerRecipeFiller(SoulEngulfingRecipe.class, SpiritRecipes.SOUL_ENGULFING.get(), SoulEngulfingDisplay::new);
    }
}
