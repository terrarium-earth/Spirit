package me.codexadrian.spirit.registry.fabric;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.recipe.PedestalRecipe;
import me.codexadrian.spirit.recipe.SoulEngulfingRecipe;
import me.codexadrian.spirit.registry.SpiritRecipes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class SpiritRecipesImpl {
    public static Supplier<RecipeType<SoulEngulfingRecipe>> SOUL_ENGULFING = registerRecipeType("soul_engulfing");
    public static Supplier<RecipeType<Tier>> TIER = registerRecipeType("soul_cage_tier");
    public static Supplier<RecipeType<MobTraitData>> MOB_TRAIT = registerRecipeType("mob_trait");
    public static Supplier<RecipeType<PedestalRecipe>> SOUL_TRANSMUTATION = registerRecipeType("soul_transmutation");


    public static Supplier<RecipeType<SoulEngulfingRecipe>> getSoulEngulfingRecipe() {
        return SOUL_ENGULFING;
    }

    public static Supplier<RecipeType<Tier>> getTierRecipe() {
        return TIER;
    }

    public static Supplier<RecipeType<MobTraitData>> getMobTraitRecipe() {
        return MOB_TRAIT;
    }

    public static Supplier<RecipeType<PedestalRecipe>> getSoulTransmutationRecipe() {
        return SOUL_TRANSMUTATION;
    }

    public static <R extends Recipe<?>, T extends RecipeType<R>> Supplier<T> registerRecipeType(String name) {
        var reg =  Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Spirit.MODID, name), SpiritRecipes.createType(name));
        return () -> (T) reg;
    }

}
