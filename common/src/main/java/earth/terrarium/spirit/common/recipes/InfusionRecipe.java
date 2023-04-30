package earth.terrarium.spirit.common.recipes;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
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

public interface InfusionRecipe<T> extends PedestalRecipe<T> {
    String ABILITY_KEY = "Ability";
    int duration();

    Ingredient receptacleIngredient();

    @Override
    default Optional<Ingredient> activationItem() {
        return Optional.of(receptacleIngredient());
    }

    @Override
    default boolean consumesActivator() {
        return true;
    }

    ItemStack getInfusionResult(ItemStack input);

    default boolean allowInfusion(ItemStack input) {
        return !input.getOrCreateTag().contains(SoulSteelArmor.ABILITY_KEY);
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
}