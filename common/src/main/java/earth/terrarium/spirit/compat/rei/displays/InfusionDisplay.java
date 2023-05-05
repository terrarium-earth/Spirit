package earth.terrarium.spirit.compat.rei.displays;

import earth.terrarium.spirit.common.recipes.InfusionRecipe;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.compat.rei.categories.InfusionRecipeCategory;
import earth.terrarium.spirit.compat.rei.categories.TransmutationRecipeCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InfusionDisplay extends BasicDisplay {
    private final InfusionRecipe<?> recipe;

    public InfusionDisplay(InfusionRecipe<?> recipe) {
        super(EntryIngredients.ofIngredients(recipe.getAllInputs()),
                Collections.singletonList(getOuputs(recipe)), Optional.of(recipe.getId()));
        this.recipe = recipe;
    }

    public static EntryIngredient getOuputs(InfusionRecipe<?> recipe) {
        EntryIngredient.Builder result = EntryIngredient.builder(recipe.activationItem().getItems().length);
        new ArrayList<>(List.of(recipe.activationItem().getItems())).forEach(stack -> result.add(EntryStacks.of(recipe.getInfusionResult(stack))));
        return result.build();
    }

    public InfusionRecipe<?> recipe() {
        return recipe;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return InfusionRecipeCategory.RECIPE;
    }
}
