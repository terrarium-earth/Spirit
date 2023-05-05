package earth.terrarium.spirit.common.recipes;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface InfusionRecipe<T> extends PedestalRecipe<T> {
    String ABILITY_KEY = "Ability";
    int duration();

    ItemStack getInfusionResult(ItemStack input);

    boolean allowInfusion(ItemStack input);
}