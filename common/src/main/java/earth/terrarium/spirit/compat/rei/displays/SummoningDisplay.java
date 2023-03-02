package earth.terrarium.spirit.compat.rei.displays;

import earth.terrarium.spirit.api.utils.SoulUtils;
import earth.terrarium.spirit.common.recipes.SummoningRecipe;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.rei.SpiritPlugin;
import earth.terrarium.spirit.compat.rei.categories.SummoningRecipeCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;

import java.util.List;
import java.util.Optional;

public class SummoningDisplay extends BasicDisplay {
    private final SummoningRecipe recipe;
    
    public SummoningDisplay(SummoningRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getAllInputs()),
                List.of(createEntityOutput(recipe)), Optional.ofNullable(recipe.getId()));
        this.recipe = recipe;
    }
    
    public SummoningRecipe recipe() {
        return recipe;
    }
    
    private static EntryIngredient createEntityOutput(SummoningRecipe recipe) {
        var nbt = new CompoundTag();
        nbt.putBoolean(SoulUtils.SOULLESS_TAG, true);
        return EntryIngredient.of(EntryStack.of(SpiritPlugin.ENTITY_INGREDIENT,
                new EntityIngredient(recipe.result(), -45F, Optional.of(nbt))));
    }
    
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SummoningRecipeCategory.RECIPE;
    }
}
