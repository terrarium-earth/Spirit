package earth.terrarium.spirit.compat.rei;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.common.registry.SpiritItems;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.rei.categories.TransmutationRecipeCategory;
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

public class SpiritPlugin implements REIClientPlugin {
    public static final EntryType<EntityIngredient> ENTITY_INGREDIENT = EntryType.deferred(new ResourceLocation(Spirit.MODID, "entity_ingredient"));
    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Spirit.MODID, "textures/gui/item_transmutation.png");


    @Override
    public void registerEntryTypes(EntryTypeRegistry registry) {
        registry.register(ENTITY_INGREDIENT, new EntityIngredientDefinition());
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new TransmutationRecipeCategory());

        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.PEDESTAL.get().asItem().getDefaultInstance()));
        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.SOUL_BASIN.get().asItem().getDefaultInstance()));
        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(Items.SOUL_SAND));
        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(SpiritItems.SOUL_FIRE_CHARGE.get()));
        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(SpiritItems.IGNITION_CRYSTAL.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(TransmutationRecipe.class, SpiritRecipes.TRANSMUTATION.get(), TransmutationDisplay::new);
    }
}
