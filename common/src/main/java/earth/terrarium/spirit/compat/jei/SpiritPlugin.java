package earth.terrarium.spirit.compat.jei;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.jei.categories.PedestalRecipeCategory;
import earth.terrarium.spirit.compat.jei.categories.SoulEngulfingCategory;
import earth.terrarium.spirit.compat.jei.ingredients.EntityIngredientHelper;
import earth.terrarium.spirit.compat.jei.ingredients.EntityRenderer;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class SpiritPlugin implements IModPlugin {
    public static final IIngredientType<EntityIngredient> ENTITY_INGREDIENT = () -> EntityIngredient.class;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Spirit.MODID, "jei");
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        IModPlugin.super.registerRecipes(registration);
        var level = Minecraft.getInstance().level;
        if(level != null) {
            registration.addRecipes(PedestalRecipeCategory.RECIPE, level.getRecipeManager().getAllRecipesFor(SpiritRecipes.TRANSMUTATION.get()));
            registration.addRecipes(SoulEngulfingCategory.RECIPE, SoulEngulfingCategory.getRecipes(level.getRecipeManager().getAllRecipesFor(SpiritRecipes.SOUL_ENGULFING.get())));
        }
    }

    @Override
    public void registerIngredients(@NotNull IModIngredientRegistration registration) {
        registration.register(ENTITY_INGREDIENT, List.of(), new EntityIngredientHelper(), new EntityRenderer());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(SpiritBlocks.PEDESTAL.get().asItem().getDefaultInstance(), PedestalRecipeCategory.RECIPE);
        registration.addRecipeCatalyst(Blocks.SOUL_SAND.asItem().getDefaultInstance(), SoulEngulfingCategory.RECIPE);
        registration.addRecipeCatalyst(Items.FLINT_AND_STEEL.getDefaultInstance(), SoulEngulfingCategory.RECIPE);
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IModPlugin.super.registerCategories(registration);
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new PedestalRecipeCategory(guiHelper));
        registration.addRecipeCategories(new SoulEngulfingCategory(guiHelper));
    }
}
