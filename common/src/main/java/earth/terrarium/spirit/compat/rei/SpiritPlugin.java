package earth.terrarium.spirit.compat.rei;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.utils.SoulUtils;
import earth.terrarium.spirit.common.recipes.ArmorInfusionRecipe;
import earth.terrarium.spirit.common.recipes.PedestalRecipe;
import earth.terrarium.spirit.common.recipes.ToolInfusionRecipe;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.rei.categories.InfusionRecipeCategory;
import earth.terrarium.spirit.compat.rei.categories.TransmutationRecipeCategory;
import earth.terrarium.spirit.compat.rei.displays.InfusionDisplay;
import earth.terrarium.spirit.compat.rei.displays.TransmutationDisplay;
import earth.terrarium.spirit.compat.rei.ingredients.EntityIngredientDefinition;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import me.shedaniel.rei.api.common.entry.type.EntryTypeRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Optional;

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
        registry.add(new InfusionRecipeCategory());

        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.PEDESTAL.get().asItem().getDefaultInstance()));
        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(SpiritBlocks.SOUL_BASIN.get().asItem().getDefaultInstance()));
        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(Items.SOUL_SAND));
        registry.addWorkstations(TransmutationRecipeCategory.RECIPE, EntryStacks.of(Items.FLINT_AND_STEEL));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerRecipeFiller(TransmutationRecipe.class, SpiritRecipes.TRANSMUTATION.get(), TransmutationDisplay::new);
        registry.registerRecipeFiller(ToolInfusionRecipe.class, SpiritRecipes.TOOL_INFUSION.get(), InfusionDisplay::new);
        registry.registerRecipeFiller(ArmorInfusionRecipe.class, SpiritRecipes.ARMOR_INFUSION.get(), InfusionDisplay::new);
    }

    public static void setupPedestal(BasicDisplay display, Rectangle bounds, int displayWidth, int displayHeight, ArrayList<Widget> widgets, PedestalRecipe<?> recipe) {
        widgets.add(Widgets.createRecipeBase(bounds));
        var startX = bounds.getCenterX() - displayWidth / 2;
        var startY = bounds.getCenterY() - displayHeight / 2;
        widgets.add(Widgets.createTexturedWidget(GUI_BACKGROUND, startX, startY, displayWidth, displayHeight));
        var totalComponents = recipe.itemInputs().size() + recipe.entityInputs().size();
        var startAngle = Math.PI / 2;
        var range = 26;
        for (int i = 0; i < recipe.itemInputs().size(); i++) {
            widgets.add(
                    Widgets.createSlot(new Point(startX + 38 + range * Math.cos(((double) i/totalComponents) * Math.PI * 2 - startAngle), startY + 35 + range * Math.sin(((double) i/totalComponents) * Math.PI * 2 - startAngle)))
                            .markInput()
                            .disableBackground()
                            .entries(EntryIngredients.ofIngredient(recipe.itemInputs().get(i)))
            );
        }

        var nbt = new CompoundTag();
        nbt.putBoolean(SoulUtils.SOULLESS_TAG, true);
        for (int i = recipe.itemInputs().size(); i < recipe.itemInputs().size() + recipe.entityInputs().size(); i++) {
            var entityTypes = recipe.entityInputs().get(i - recipe.itemInputs().size()).getEntities().map(entityType -> new EntityIngredient(entityType, -45F, Optional.of(nbt))).toList();
            widgets.add(
                    Widgets.createSlot(new Point(startX + 38 + range * Math.cos(((double) i/totalComponents) * Math.PI * 2 - startAngle), startY + 35 + range * Math.sin(((double) i/totalComponents) * Math.PI * 2 - startAngle)))
                            .markInput()
                            .disableBackground()
                            .entries(EntryIngredients.of(SpiritPlugin.ENTITY_INGREDIENT, entityTypes))
            );
        }
    }

}
