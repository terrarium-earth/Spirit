package jei.categories;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.recipes.PedestalRecipe;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.jei.SpiritPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 *
 * @author Team Resourceful
 */

public abstract class BaseCategory<T> implements IRecipeCategory<T> {

    private static final ResourceLocation ICONS = new ResourceLocation(Spirit.MODID, "textures/gui/icons.png");

    private final RecipeType<T> recipeType;
    private final Component localizedName;
    private final IDrawable background;
    private final IDrawable icon;

    public final IDrawable slot;
    public final IDrawable outputSlot;

    protected BaseCategory(IGuiHelper guiHelper, RecipeType<T> recipeType, Component localizedName, IDrawable background, IDrawable icon) {
        this.recipeType = recipeType;
        this.localizedName = localizedName;
        this.background = background;
        this.icon = icon;

        this.outputSlot = guiHelper.createDrawable(ICONS, 0, 0, 26, 26);
        this.slot = guiHelper.getSlotDrawable();
    }

    @Override
    public @NotNull RecipeType<T> getRecipeType() {
        return this.recipeType;
    }

    @Override
    public @NotNull Component getTitle() {
        return localizedName;
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return background;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    public static void setupPedestalRecipe(@NotNull IRecipeLayoutBuilder builder, PedestalRecipe<?> recipe, @NotNull IFocusGroup focuses) {
        var totalComponents = recipe.itemInputs().size() + recipe.entityInputs().size();
        var startAngle = Math.PI / 2;
        var range = 26;
        for (int i = 0; i < recipe.itemInputs().size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, (int) (38 + range * Math.cos(((double) i/totalComponents) * Math.PI * 2 - startAngle)), (int) (35 + range * Math.sin(((double) i/totalComponents) * Math.PI * 2 - startAngle))).addIngredients(recipe.getIngredients().get(i));
        }

        var nbt = new CompoundTag();
        nbt.putBoolean("Corrupted", true);
        for (int i = recipe.itemInputs().size(); i < recipe.itemInputs().size() + recipe.entityInputs().size(); i++) {
            var toRender = recipe.entityInputs().get(i - recipe.itemInputs().size()).getEntities().map(entityType -> new EntityIngredient(entityType, -45F, Optional.of(nbt))).toList();
            builder.addSlot(RecipeIngredientRole.INPUT, (int) (38 + range * Math.cos(((double) i/totalComponents) * Math.PI * 2 - startAngle)), (int) (35 + range * Math.sin(((double) i/totalComponents) * Math.PI * 2 - startAngle))).addIngredients(SpiritPlugin.ENTITY_INGREDIENT, toRender);
        }
    }
}