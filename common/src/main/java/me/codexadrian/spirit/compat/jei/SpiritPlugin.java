package me.codexadrian.spirit.compat.jei;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.SpiritConfig;
import me.codexadrian.spirit.compat.jei.categories.PedestalRecipeCategory;
import me.codexadrian.spirit.compat.jei.categories.SoulCageCategory;
import me.codexadrian.spirit.compat.jei.ingredients.EntityIngredient;
import me.codexadrian.spirit.compat.jei.ingredients.EntityIngredientHelper;
import me.codexadrian.spirit.compat.jei.ingredients.EntityRenderer;
import me.codexadrian.spirit.platform.Services;
import me.codexadrian.spirit.registry.SpiritBlocks;
import me.codexadrian.spirit.registry.SpiritMisc;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
        if(!Services.PLATFORM.isModLoaded("chipped") && SpiritConfig.showChippedError()) {
            registration.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, SpiritBlocks.SOUL_GLASS_BLOCKS.stream().map(blockSupplier -> new ItemStack(blockSupplier.get().asItem())).toList());
        }
        var level = Minecraft.getInstance().level;
        if(level != null) {
            registration.addRecipes(PedestalRecipeCategory.RECIPE, level.getRecipeManager().getAllRecipesFor(SpiritMisc.SOUL_TRANSMUTATION_RECIPE.get()));
            registration.addRecipes(SoulCageCategory.RECIPE, level.getRecipeManager().getAllRecipesFor(SpiritMisc.TIER_RECIPE.get()));
        }
    }

    @Override
    public void registerIngredients(@NotNull IModIngredientRegistration registration) {
        registration.register(ENTITY_INGREDIENT, List.of(), new EntityIngredientHelper(), new EntityRenderer());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(SpiritBlocks.SOUL_PEDESTAL.get().asItem().getDefaultInstance(), PedestalRecipeCategory.RECIPE);
        registration.addRecipeCatalyst(SpiritBlocks.PEDESTAL.get().asItem().getDefaultInstance(), PedestalRecipeCategory.RECIPE);
        registration.addRecipeCatalyst(SpiritBlocks.SOUL_CAGE.get().asItem().getDefaultInstance(), SoulCageCategory.RECIPE);
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IModPlugin.super.registerCategories(registration);
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new PedestalRecipeCategory(guiHelper));
        registration.addRecipeCategories(new SoulCageCategory(guiHelper));
    }
}
