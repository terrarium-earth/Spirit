package me.codexadrian.spirit.compat.rei.displays;

import me.codexadrian.spirit.compat.jei.ingredients.EntityIngredient;
import me.codexadrian.spirit.compat.rei.SpiritPlugin;
import me.codexadrian.spirit.compat.rei.categories.PedestalRecipeCategory;
import me.codexadrian.spirit.recipe.PedestalRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.nbt.CompoundTag;

import java.util.List;
import java.util.Optional;

public class PedestalDisplay extends BasicDisplay {
    private final PedestalRecipe recipe;
    
    public PedestalDisplay(PedestalRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.ingredients()),
                List.of(createEntityOutput(recipe)), Optional.ofNullable(recipe.getId()));
        this.recipe = recipe;
    }
    
    public PedestalRecipe recipe() {
        return recipe;
    }
    
    private static EntryIngredient createEntityOutput(PedestalRecipe recipe) {
        var nbt = new CompoundTag();
        nbt.putBoolean("Corrupted", true);
        return EntryIngredient.of(EntryStack.of(SpiritPlugin.ENTITY_INGREDIENT,
                new EntityIngredient(recipe.entityOutput(), -45F, recipe.shouldSummon() ? Optional.empty() : Optional.of(nbt))));
    }
    
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return PedestalRecipeCategory.RECIPE;
    }
}
