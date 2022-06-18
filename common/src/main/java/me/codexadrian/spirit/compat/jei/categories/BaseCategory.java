package me.codexadrian.spirit.compat.jei.categories;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.recipe.SoulEngulfingRecipe;
import me.codexadrian.spirit.registry.SpiritMisc;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
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
}