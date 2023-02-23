package earth.terrarium.spirit.common.recipes;

import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Optional;

public interface PedestalRecipe<T> {
    Optional<Ingredient> activationItem();
    boolean consumesActivator();
    List<Ingredient> itemInputs();
    List<SoulIngredient> entityInputs();
    int duration();
    T output();
}
