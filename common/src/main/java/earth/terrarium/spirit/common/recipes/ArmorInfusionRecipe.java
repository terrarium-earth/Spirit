package earth.terrarium.spirit.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import earth.terrarium.spirit.common.registry.SpiritItems;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.core.registries.BuiltInRegistries;
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
                                  SoulIngredient entityInput, ArmorAbility result,
                                  int duration) implements InfusionRecipe {

    public static Codec<ArmorInfusionRecipe> codec(ResourceLocation location) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(location),
                Codec.STRING.xmap(EnchantmentCategory::valueOf, EnchantmentCategory::toString).optionalFieldOf("input_type").forGetter(ArmorInfusionRecipe::inputType),
                SoulIngredient.CODEC.fieldOf("entity_input").forGetter(ArmorInfusionRecipe::entityInput),
                ArmorAbilityManager.getAbilityRegistry().byNameCodec().fieldOf("result").forGetter(ArmorInfusionRecipe::result),
                Codec.INT.fieldOf("duration").forGetter(ArmorInfusionRecipe::duration)
        ).apply(instance, ArmorInfusionRecipe::new));
    }

    @Override
    public boolean matches(Container container, Level level) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpiritRecipes.ARMOR_INFUSION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SpiritRecipes.ARMOR_INFUSION.get();
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

    @Override
    public List<Ingredient> getAllInputs() {
        List<Ingredient> allInputs = InfusionRecipe.super.getAllInputs();
        allInputs.addAll(SpiritItems.ARMOR.stream().filter(item -> inputType().map(category -> category.canEnchant(item.get())).orElse(true)).map(RegistryEntry::get).map(Ingredient::of).toList());
        return allInputs;
    }
}
