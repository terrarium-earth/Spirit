package me.codexadrian.spirit.compat.forge.tinkers.data;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.compat.forge.tinkers.SpiritFluids;
import me.codexadrian.spirit.registry.SpiritBlocks;
import me.codexadrian.spirit.registry.SpiritItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.recipe.ICommonRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.IToolRecipeHelper;

import java.util.function.Consumer;

public class SpiritRecipes extends RecipeProvider implements IMaterialRecipeHelper, IToolRecipeHelper, ISmelteryRecipeHelper, ICommonRecipeHelper {
    public SpiritRecipes(DataGenerator arg) {
        super(arg);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        metalCasting(consumer, SpiritFluids.MOLTEN_SOUL_STEEL_FLUID, SpiritBlocks.SOUL_STEEL_BLOCK.get(), SpiritItems.SOUL_STEEL_INGOT.get(), SpiritItems.SOUL_STEEL_NUGGET.get(), "smeltery/casting", "soul_steel");
        metalMelting(consumer, SpiritFluids.MOLTEN_SOUL_STEEL.get(), "soul_steel", false, false, "smeltery/melting", false);
    }

    @Override
    public String getModId() {
        return Spirit.MODID;
    }
}
