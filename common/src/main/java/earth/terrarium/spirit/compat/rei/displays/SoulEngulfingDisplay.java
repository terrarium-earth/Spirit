package earth.terrarium.spirit.compat.rei.displays;

import earth.terrarium.spirit.common.recipes.SoulEngulfingRecipe;
import earth.terrarium.spirit.compat.SoulEngulfingRecipeWrapper;
import earth.terrarium.spirit.compat.rei.categories.SoulEngulfingCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class SoulEngulfingDisplay implements Display {
    private final SoulEngulfingRecipeWrapper wrapper;
    private final EntryIngredient input;
    private final EntryIngredient catalyst;
    private final EntryIngredient output;

    public SoulEngulfingDisplay(SoulEngulfingRecipe recipe) {
        this(new SoulEngulfingRecipeWrapper(recipe));
    }

    public SoulEngulfingDisplay(SoulEngulfingRecipeWrapper wrapper) {
        this.wrapper = wrapper;
        var recipe = wrapper.getRecipe();
        var items = new ArrayList<ItemStack>();
        var blocks = recipe.input().multiblock().keys().values();
        var holderSets = blocks.stream().flatMap(predicate -> {
            if (predicate.blocks().isPresent()) return predicate.blocks().get().stream();
            return Stream.of();
        }).toList();
        for (Holder<Block> holderSet : holderSets) {
            items.add(holderSet.value().asItem().getDefaultInstance());
        }
        input = EntryIngredients.ofIngredient(recipe.input().item());
        catalyst = EntryIngredients.ofIngredient(Ingredient.of(items.stream()));
        output = EntryIngredients.of(recipe.getResultItem());
    }

    @Override
    public List<EntryIngredient> getInputEntries() {
        return List.of(input, catalyst);
    }

    @Override
    public List<EntryIngredient> getRequiredEntries() {
        return List.of(input);
    }

    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of(output);
    }

    public SoulEngulfingRecipeWrapper getWrapper() {
        return wrapper;
    }

    public EntryIngredient getInput() {
        return input;
    }

    public EntryIngredient getCatalyst() {
        return catalyst;
    }

    public EntryIngredient getOutput() {
        return output;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SoulEngulfingCategory.RECIPE;
    }
}
