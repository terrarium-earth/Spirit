package earth.terrarium.spirit.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.recipes.IngredientCodec;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbilityManager;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import earth.terrarium.spirit.common.item.tools.SoulSteelTool;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ToolInfusionRecipe(ResourceLocation id, Ingredient activationItem,
                                 List<SoulIngredient> entityInputs, List<Ingredient> itemInputs,
                                 ToolAbility result,
                                 int duration) implements InfusionRecipe<ToolAbility> {

    public static Codec<ToolInfusionRecipe> codec(ResourceLocation location) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(location),
                IngredientCodec.CODEC.fieldOf("activatorIngredient").forGetter(ToolInfusionRecipe::activationItem),
                SoulIngredient.CODEC.listOf().fieldOf("entityIngredients").forGetter(ToolInfusionRecipe::entityInputs),
                IngredientCodec.CODEC.listOf().fieldOf("ingredients").forGetter(ToolInfusionRecipe::itemInputs),
                Codec.STRING.fieldOf("result").xmap(ToolAbilityManager::getAbility, ToolAbilityManager::getName).forGetter(ToolInfusionRecipe::result),
                Codec.INT.fieldOf("duration").orElse(60).forGetter(ToolInfusionRecipe::duration)
        ).apply(instance, ToolInfusionRecipe::new));
    }

    @Override
    public boolean matches(Container container, Level level) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SpiritRecipes.TOOL_INFUSION_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return SpiritRecipes.TOOL_INFUSION.get();
    }

    @Override
    public ItemStack getInfusionResult(ItemStack input) {
        ItemStack result = input.copy();
        if (result.getItem() instanceof SoulSteelTool tool) {
            tool.addAbility(result(), result);
        }
        return result;
    }

    @Override
    public boolean allowInfusion(ItemStack input) {
        return input.getItem() instanceof SoulSteelTool tool && tool.allowInfusion(input);
    }

    public static @Nullable ToolInfusionRecipe getRecipeFromId(ResourceLocation name, RecipeManager manager) {
        return (ToolInfusionRecipe) manager.getAllRecipesFor(SpiritRecipes.TOOL_INFUSION.get()).stream()
                .filter(recipe -> recipe.getId().equals(name))
                .findFirst()
                .orElse(null);
    }
}
