package earth.terrarium.spirit.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.recipes.IngredientCodec;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record TransmutationRecipe(ResourceLocation id, Ingredient activationItem,
                                  List<SoulIngredient> entityInputs, List<Ingredient> itemInputs,
                                  ItemStack result, int duration, boolean consumesFlame) implements CodecRecipe<Container>, PedestalRecipe<ItemStack> {
    public static Codec<TransmutationRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                IngredientCodec.CODEC.fieldOf("activatorIngredient").forGetter(TransmutationRecipe::activationItem),
                SoulIngredient.CODEC.listOf().fieldOf("entityIngredients").forGetter(TransmutationRecipe::entityInputs),
                IngredientCodec.CODEC.listOf().fieldOf("ingredients").forGetter(TransmutationRecipe::itemInputs),
                ItemStackCodec.CODEC.fieldOf("result").forGetter(TransmutationRecipe::result),
                Codec.INT.fieldOf("duration").orElse(60).forGetter(TransmutationRecipe::duration),
                Codec.BOOL.fieldOf("consumesFlame").orElse(false).forGetter(TransmutationRecipe::consumesFlame)
        ).apply(instance, TransmutationRecipe::new));
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SpiritRecipes.TRANSMUTATION_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return SpiritRecipes.TRANSMUTATION.get();
    }
}
