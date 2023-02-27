package earth.terrarium.spirit.compat.jei.categories;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.recipes.SummoningRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.jei.SpiritPlugin;
import earth.terrarium.spirit.compat.jei.ingredients.BigEntityRenderer;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedestalRecipeCategory extends BaseCategory<SummoningRecipe> {
    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Spirit.MODID, "textures/gui/soul_transmutation.png");
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "soul_transmutation");
    public static final RecipeType<SummoningRecipe> RECIPE = new RecipeType<>(ID, SummoningRecipe.class);
    private static final List<int[]> slots = List.of(
            new int[]{32, 11},
            new int[]{55, 18},
            new int[]{62, 41},
            new int[]{55, 64},
            new int[]{32, 71},
            new int[]{9, 64},
            new int[]{2, 41},
            new int[]{9, 18}
    );

    public PedestalRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper,
                RECIPE,
                Component.translatable("spirit.jei.soul_transmutation.title"),
                guiHelper.drawableBuilder(GUI_BACKGROUND, 0,0, 150, 100).build(),
                guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, SpiritBlocks.SUMMONING_PEDESTAL.get().asItem().getDefaultInstance()));
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, SummoningRecipe recipe, @NotNull IFocusGroup focuses) {
        for (int i = 0; i < Math.min(recipe.getIngredients().size(), 8); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, slots.get(i)[0], slots.get(i)[1]).addIngredients(recipe.getIngredients().get(i));
        }
        var nbt = new CompoundTag();
        nbt.putBoolean("Corrupted", true);
        var entityTypes = recipe.entityInputs().stream().flatMap(soulIngredient -> soulIngredient.getRawValues().stream().flatMap(either -> either.left().isPresent() ? either.left().get().getSouls().stream() : either.right().get().getSouls().stream())).map(entityType -> new EntityIngredient(entityType.getEntity(), -45F, Optional.of(nbt))).toList();
        if(recipe.activationItem().isPresent()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 93, 21).addIngredients(recipe.activationItem().get()).addTooltipCallback((recipeSlotView, tooltip) -> {
                if(recipe.consumesActivator()) tooltip.add(Component.translatable("spirit.jei.soul_transmutation.consumes").withStyle(ChatFormatting.RED));
            });
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 37).addIngredients(SpiritPlugin.ENTITY_INGREDIENT, entityTypes).setCustomRenderer(SpiritPlugin.ENTITY_INGREDIENT, BigEntityRenderer.INSTANCE);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 124, 37).addIngredient(SpiritPlugin.ENTITY_INGREDIENT, new EntityIngredient(recipe.result(), -45F, recipe.outputNbt())).setCustomRenderer(SpiritPlugin.ENTITY_INGREDIENT, BigEntityRenderer.INSTANCE);
    }

    @Override
    public List<Component> getTooltipStrings(SummoningRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        List<Component> components = new ArrayList<>();
        if(recipe.activationItem().isEmpty() && mouseX > 91 && mouseX < 111 && mouseY > 19 && mouseY < 39) {
            components.add(Component.translatable("spirit.jei.soul_transmutation.empty_hand"));
        }
        return components;
    }
}
