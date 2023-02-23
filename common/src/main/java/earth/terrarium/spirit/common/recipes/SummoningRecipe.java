package earth.terrarium.spirit.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.recipes.IngredientCodec;
import com.teamresourceful.resourcefullib.common.codecs.tags.HolderSetCodec;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public record SummoningRecipe(ResourceLocation id, List<SoulIngredient> entityInputs, Optional<Ingredient> activationItem, boolean consumesActivator, List<Ingredient> itemInputs,
                              EntityType<?> output, int duration,
                              Optional<CompoundTag> outputNbt) implements CodecRecipe<Container>, PedestalRecipe<EntityType<?>> {
    public static Codec<SummoningRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                SoulIngredient.CODEC.listOf().fieldOf("entityInput").forGetter(SummoningRecipe::entityInputs),
                IngredientCodec.CODEC.optionalFieldOf("activatorItem").forGetter(SummoningRecipe::activationItem),
                Codec.BOOL.fieldOf("consumesActivator").orElse(false).forGetter(SummoningRecipe::consumesActivator),
                IngredientCodec.CODEC.listOf().fieldOf("itemInputs").forGetter(SummoningRecipe::itemInputs),
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entityOutput").forGetter(SummoningRecipe::output),
                Codec.INT.fieldOf("duration").orElse(60).forGetter(SummoningRecipe::duration),
                CompoundTag.CODEC.optionalFieldOf("outputNbt").forGetter(SummoningRecipe::outputNbt)
        ).apply(instance, SummoningRecipe::new));
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpiritRecipes.SUMMONING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SpiritRecipes.SUMMONING.get();
    }

    public static List<SummoningRecipe> getRecipesForEntity(SoulStack entity, ItemStack stack, RecipeManager manager) {
        return manager.getAllRecipesFor(SpiritRecipes.SUMMONING.get()).stream().filter(recipe -> {
            boolean stackMatches;
            if(recipe.activationItem().isPresent()) {
                stackMatches = recipe.activationItem().get().test(stack);
            } else {
                stackMatches = stack.isEmpty();
            }
            return stackMatches;
        }).toList();
    }

    public static Optional<SummoningRecipe> getEffect(String id, RecipeManager manager) {
        return (Optional<SummoningRecipe>) manager.byKey(ResourceLocation.tryParse(id));
    }
}
