package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.recipes.SoulEngulfingRecipe;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class SpiritRecipes {
    public static final ResourcefulRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_SERIALIZER, Spirit.MODID);
    public static final ResourcefulRegistry<RecipeType<?>> RECIPE_TYPES = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_TYPE, Spirit.MODID);

    //SoulEngulfing
    public static final RegistryEntry<RecipeType<SoulEngulfingRecipe>> SOUL_ENGULFING = RECIPE_TYPES.register("soul_engulfing", () -> CodecRecipeType.of("soul_engulfing"));
    public static final RegistryEntry<RecipeSerializer<SoulEngulfingRecipe>> SOUL_ENGULFING_SERIALIZER = RECIPE_SERIALIZERS.register("soul_engulfing", () -> new CodecRecipeSerializer<>(SOUL_ENGULFING.get(), SoulEngulfingRecipe::codec));

    //Transmutation
    public static final RegistryEntry<RecipeType<TransmutationRecipe>> TRANSMUTATION = RECIPE_TYPES.register("transmutation", () -> CodecRecipeType.of("transmutation"));
    public static final RegistryEntry<RecipeSerializer<TransmutationRecipe>> TRANSMUTATION_SERIALIZER = RECIPE_SERIALIZERS.register("transmutation", () -> new CodecRecipeSerializer<>(TRANSMUTATION.get(), TransmutationRecipe::codec));
}
