package earth.terrarium.spirit.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.recipes.IngredientCodec;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.FluidIngredient;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public record TransmutationRecipe(ResourceLocation id, Optional<Ingredient> activationItem, boolean consumesActivator, List<SoulIngredient> entityInputs, List<Ingredient> itemInputs,
                                  List<FluidIngredient> essenceInputs, ItemStack output, int duration) implements CodecRecipe<Container>, PedestalRecipe<ItemStack> {
    public static Codec<TransmutationRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                IngredientCodec.CODEC.optionalFieldOf("activatorItem").forGetter(TransmutationRecipe::activationItem),
                Codec.BOOL.fieldOf("consumesActivator").orElse(false).forGetter(TransmutationRecipe::consumesActivator),
                SoulIngredient.CODEC.listOf().fieldOf("entityInput").forGetter(TransmutationRecipe::entityInputs),
                IngredientCodec.CODEC.listOf().fieldOf("itemInputs").forGetter(TransmutationRecipe::itemInputs),
                FluidIngredient.CODEC.listOf().fieldOf("essenceInputs").forGetter(TransmutationRecipe::essenceInputs),
                ItemStackCodec.CODEC.fieldOf("itemOutput").forGetter(TransmutationRecipe::output),
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

    public static List<TransmutationRecipe> getRecipesForEntity(SoulStack entity, ItemStack stack, RecipeManager manager) {
        return manager.getAllRecipesFor(SpiritRecipes.TRANSMUTATION.get()).stream().filter(recipe -> {
            boolean stackMatches;
            if(recipe.activationItem().isPresent()) {
                stackMatches = recipe.activationItem().get().test(stack);
            } else {
                stackMatches = stack.isEmpty();
            }
            return recipe.entityInputs.test(entity) && stackMatches;
        }).toList();
    }

    public static Optional<TransmutationRecipe> getEffect(String id, RecipeManager manager) {
        return (Optional<TransmutationRecipe>) manager.byKey(ResourceLocation.tryParse(id));
    }

    public ItemEntity createEntity(Level level, double x, double y, double z) {
        return new ItemEntity(level, x, y, z, output.copy());
    }
}
