package earth.terrarium.spirit.common.recipes;

import earth.terrarium.spirit.api.armor_abilities.ArmorAbility;
import earth.terrarium.spirit.api.armor_abilities.ArmorAbilityManager;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public record ArmorInfusionRecipe(ResourceLocation id, Optional<EnchantmentCategory> inputType,
                                  List<SoulIngredient> entityInputs, List<Ingredient> itemInputs,
                                  ArmorAbility result,
                                  int duration) implements InfusionRecipe {

    @Override
    public boolean matches(Container container, Level level) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    @Override
    public ItemStack getInfusionResult(ItemStack input) {
        var result = input.copy();
        result.getOrCreateTag().putString(SoulSteelArmor.ABILITY_KEY, ArmorAbilityManager.getAbilityRegistry().getKey(result()).toString());
        return result;
    }

    @Override
    public boolean allowInfusion(ItemStack input) {
        return input.getItem() instanceof SoulSteelArmor && !input.getOrCreateTag().contains(SoulSteelArmor.ABILITY_KEY) && InfusionRecipe.super.allowInfusion(input);
    }
}
