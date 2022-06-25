package me.codexadrian.spirit.registry.forge;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.recipe.PedestalRecipe;
import me.codexadrian.spirit.recipe.SoulEngulfingRecipe;
import me.codexadrian.spirit.registry.SpiritRecipes;
import net.minecraft.core.Registry;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class SpiritRecipesImpl {
    public static DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE.key(), Spirit.MODID);

    public static RecipeType<SoulEngulfingRecipe> SOUL_ENGULFING = SpiritRecipes.createType("soul_engulfing");
    public static RecipeType<Tier> TIER = SpiritRecipes.createType("soul_cage_tier");
    public static RecipeType<MobTraitData> MOB_TRAIT = SpiritRecipes.createType("mob_trait");
    public static RecipeType<PedestalRecipe> SOUL_TRANSMUTATION = SpiritRecipes.createType("soul_transmutation");

    public static Supplier<RecipeType<SoulEngulfingRecipe>> getSoulEngulfingRecipe() {
        return () -> SOUL_ENGULFING;
    }

    public static Supplier<RecipeType<Tier>> getTierRecipe() {
        return () -> TIER;
    }

    public static Supplier<RecipeType<MobTraitData>> getMobTraitRecipe() {
        return () -> MOB_TRAIT;
    }

    public static Supplier<RecipeType<PedestalRecipe>> getSoulTransmutationRecipe() {
        return () -> SOUL_TRANSMUTATION;
    }
}
