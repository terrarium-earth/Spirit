package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeSerializer;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipeType;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.recipes.ArmorInfusionRecipe;
import earth.terrarium.spirit.common.recipes.ToolInfusionRecipe;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class SpiritRecipes {
    public static final ResourcefulRegistry<RecipeSerializer<?>> RECIPE_SERIALIZERS = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_SERIALIZER, Spirit.MODID);
    public static final ResourcefulRegistry<RecipeType<?>> RECIPE_TYPES = ResourcefulRegistries.create(BuiltInRegistries.RECIPE_TYPE, Spirit.MODID);

    //Transmutation
    public static final RegistryEntry<RecipeType<TransmutationRecipe>> TRANSMUTATION = RECIPE_TYPES.register("transmutation", () -> CodecRecipeType.of("transmutation"));
    public static final RegistryEntry<RecipeSerializer<TransmutationRecipe>> TRANSMUTATION_SERIALIZER = RECIPE_SERIALIZERS.register("transmutation", () -> new CodecRecipeSerializer<>(TRANSMUTATION.get(), TransmutationRecipe::codec));

    //ArmorInfusion
    public static final RegistryEntry<RecipeType<ArmorInfusionRecipe>> ARMOR_INFUSION = RECIPE_TYPES.register("armor_infusion", () -> CodecRecipeType.of("armor_infusion"));
    public static final RegistryEntry<RecipeSerializer<ArmorInfusionRecipe>> ARMOR_INFUSION_SERIALIZER = RECIPE_SERIALIZERS.register("armor_infusion", () -> new CodecRecipeSerializer<>(ARMOR_INFUSION.get(), ArmorInfusionRecipe::codec));

    //ArmorInfusion
    public static final RegistryEntry<RecipeType<ToolInfusionRecipe>> TOOL_INFUSION = RECIPE_TYPES.register("tool_infusion", () -> CodecRecipeType.of("tool_infusion"));
    public static final RegistryEntry<RecipeSerializer<ToolInfusionRecipe>> TOOL_INFUSION_SERIALIZER = RECIPE_SERIALIZERS.register("tool_infusion", () -> new CodecRecipeSerializer<>(TOOL_INFUSION.get(), ToolInfusionRecipe::codec));

}
