package me.codexadrian.spirit.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.recipe.PedestalRecipe;
import me.codexadrian.spirit.recipe.SoulEngulfingRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

public class SpiritRecipes {
    
    public static void registerAll() {
    }

    @ExpectPlatform
    public static Supplier<RecipeType<SoulEngulfingRecipe>>  getSoulEngulfingRecipe() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Supplier<RecipeType<Tier>> getTierRecipe() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static  Supplier<RecipeType<MobTraitData>>  getMobTraitRecipe() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Supplier<RecipeType<PedestalRecipe>>  getSoulTransmutationRecipe() {
        throw new AssertionError();
    }

    public static <R extends Recipe<?>> RecipeType<R> createType(String type) {
        return new RecipeType<>() {
            @Override
            public String toString() {
                return type;
            }
        };
    }
}
