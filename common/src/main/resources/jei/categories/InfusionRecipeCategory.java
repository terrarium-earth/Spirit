package jei.categories;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.recipes.InfusionRecipe;
import earth.terrarium.spirit.common.registry.SpiritItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class InfusionRecipeCategory extends BaseCategory<InfusionRecipe<?>> {
    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Spirit.MODID, "textures/gui/soul_transmutation.png");
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "soul_transmutation");
    public static final RecipeType<InfusionRecipe<?>> RECIPE = new RecipeType<>(ID, InfusionRecipe.class);

    public InfusionRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper,
                RECIPE,
                Component.translatable("spirit.jei.soul_transmutation.title"),
                guiHelper.drawableBuilder(GUI_BACKGROUND, 0,0, 150, 100).build(),
                guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, SpiritItems.SCYTHE.get().getDefaultInstance()));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, InfusionRecipe<?> recipe, @NotNull IFocusGroup focuses) {
        BaseCategory.setupPedestalRecipe(builder, recipe, focuses);

        builder.addSlot(RecipeIngredientRole.CATALYST, 93, 21).addIngredients(recipe.activationItem());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 122, 37).addItemStacks(Arrays.stream(recipe.activationItem().getItems()).map(recipe::getInfusionResult).toList());
    }
}
