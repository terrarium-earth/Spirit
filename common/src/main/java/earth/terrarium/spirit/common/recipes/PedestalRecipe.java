package earth.terrarium.spirit.common.recipes;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PedestalRecipe<T> extends CodecRecipe<Container> {
    Optional<Ingredient> activationItem();

    boolean consumesActivator();

    List<Ingredient> itemInputs();

    List<SoulIngredient> entityInputs();

    int duration();

    T result();

    static <Q extends PedestalRecipe<?>> List<Q> getRecipesForEntity(RecipeType<Q> type, ItemStack stack, RecipeManager manager) {
        return manager.getAllRecipesFor(type).stream().filter(recipe -> {
            boolean stackMatches;
            if (recipe.activationItem().isPresent()) {
                stackMatches = recipe.activationItem().get().test(stack);
            } else {
                stackMatches = stack.isEmpty();
            }
            return stackMatches;
        }).toList();
    }

    default List<Ingredient> getAllInputs() {
        List<Ingredient> inputs = new ArrayList<>(itemInputs());
        if (activationItem().isPresent()) {
            inputs.add(activationItem().get());
        }
        inputs.addAll(entityInputs().stream().flatMap(SoulIngredient::getEntities).map(entityType -> Ingredient.of(SpawnEggItem.byId(entityType))).toList());
        return inputs;
    }
}
