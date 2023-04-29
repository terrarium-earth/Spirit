package earth.terrarium.spirit.common.recipes;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface InfusionRecipe extends CodecRecipe<Container> {
    Optional<EnchantmentCategory> inputType();

    SoulIngredient entityInput();

    int duration();

    ItemStack getInfusionResult(ItemStack input);

    default boolean allowInfusion(ItemStack input) {
        return inputType().map(category -> category.canEnchant(input.getItem())).orElse(true) ;
    }

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
        List<Ingredient> inputs = new ArrayList<>();
        inputs.addAll(entityInput().getEntities().map(entity -> Ingredient.of(SpawnEggItem.byId(entity))).toList());
        return inputs;
    }
}