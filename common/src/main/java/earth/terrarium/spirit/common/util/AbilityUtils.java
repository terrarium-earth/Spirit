package earth.terrarium.spirit.common.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class AbilityUtils {
    //get the cooked result of an item
    public static ItemStack getCookedResult(Level level, ItemStack itemStack) {
        ItemStack result = itemStack.copy();
        Optional<SmeltingRecipe> first = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING).stream().filter(recipe -> recipe.getIngredients().get(0).test(result)).findFirst();
        if (first.isPresent()) {
            return first.get().getResultItem(level.registryAccess()).copy();
        }
        return ItemStack.EMPTY;
    }
}
