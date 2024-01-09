package earth.terrarium.spirit.api.rituals.components;

import earth.terrarium.spirit.compat.rei.ComponentUtils;
import earth.terrarium.spirit.compat.rei.categories.TransmutationRecipeCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public interface RitualComponent<T extends RitualComponent<T>> {
    boolean matches(Level level, BlockPos blockPos, BlockPos ritualPos);
    void onRitualComplete(Level level, BlockPos componentPos, BlockPos ritualPos);
    List<Ingredient> getIngredients();

    ComponentUtils.ReiPlacer getREIPlacer();

    RitualComponentSerializer<T> getSerializer();
}
