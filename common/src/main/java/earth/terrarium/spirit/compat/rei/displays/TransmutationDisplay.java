package earth.terrarium.spirit.compat.rei.displays;

import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.compat.rei.categories.TransmutationRecipeCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.List;
import java.util.Optional;

public class TransmutationDisplay extends BasicDisplay {
    private final TransmutationRecipe recipe;

    public TransmutationDisplay(TransmutationRecipe recipe) {
        super(EntryIngredients.ofIngredients(recipe.getAllInputs()),
                List.of(EntryIngredients.of(recipe.result())), Optional.ofNullable(recipe.getId()));
        this.recipe = recipe;
    }
    
    public TransmutationRecipe recipe() {
        return recipe;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return TransmutationRecipeCategory.RECIPE;
    }
}
