package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeType;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.recipes.SoulEngulfingRecipe;
import earth.terrarium.spirit.common.recipes.SummoningRecipe;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class SpiritRecipes {
    public static final ResourcefulRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_SERIALIZER, Spirit.MODID);
    public static final ResourcefulRegistry<RecipeType<?>> RECIPE_TYPES = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_TYPE, Spirit.MODID);

    //Summoning
    public static final Supplier<RecipeType<SummoningRecipe>> SUMMONING = RECIPE_TYPES.register("summoning", () -> CodecRecipeType.of("summoning"));
    public static final Supplier<RecipeSerializer<SummoningRecipe>> SUMMONING_SERIALIZER = RECIPE_SERIALIZERS.register("summoning", () -> new CodecRecipeSerializer<>(SUMMONING.get(), SummoningRecipe::codec));

    //SoulEngulfing
    public static final Supplier<RecipeType<SoulEngulfingRecipe>> SOUL_ENGULFING = RECIPE_TYPES.register("soul_engulfing", () -> CodecRecipeType.of("soul_engulfing"));
    public static final Supplier<RecipeSerializer<SoulEngulfingRecipe>> SOUL_ENGULFING_SERIALIZER = RECIPE_SERIALIZERS.register("soul_engulfing", () -> new CodecRecipeSerializer<>(SOUL_ENGULFING.get(), SoulEngulfingRecipe::codec));

    //Transmutation
    public static final Supplier<RecipeType<TransmutationRecipe>> TRANSMUTATION = RECIPE_TYPES.register("transmutation", () -> CodecRecipeType.of("transmutation"));
    public static final Supplier<RecipeSerializer<TransmutationRecipe>> TRANSMUTATION_SERIALIZER = RECIPE_SERIALIZERS.register("transmutation", () -> new CodecRecipeSerializer<>(TRANSMUTATION.get(), TransmutationRecipe::codec));
}
