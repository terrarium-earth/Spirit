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

import java.util.List;
import java.util.Optional;

public record TransmutationRecipe(ResourceLocation id, Optional<Ingredient> activationItem,
                                  List<SoulIngredient> entityInputs, List<Ingredient> itemInputs,
                                  ItemStack result,
                                  int duration) implements CodecRecipe<Container>, PedestalRecipe<ItemStack> {
    public static Codec<TransmutationRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                IngredientCodec.CODEC.optionalFieldOf("activatorItem").forGetter(TransmutationRecipe::activationItem),
                SoulIngredient.CODEC.listOf().fieldOf("entityInputs").forGetter(TransmutationRecipe::entityInputs),
                IngredientCodec.CODEC.listOf().fieldOf("itemInputs").forGetter(TransmutationRecipe::itemInputs),
                ItemStackCodec.CODEC.fieldOf("result").forGetter(TransmutationRecipe::result),
                Codec.INT.fieldOf("duration").orElse(60).forGetter(TransmutationRecipe::duration)
        ).apply(instance, TransmutationRecipe::new));
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpiritRecipes.TRANSMUTATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SpiritRecipes.TRANSMUTATION.get();
    }

    @Override
    public boolean consumesActivator() {
        return true;
    }
}
