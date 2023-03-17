package earth.terrarium.spirit.common.util;

import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.blockentity.PedestalBlockEntity;
import earth.terrarium.spirit.common.blockentity.SoulBasinBlockEntity;
import earth.terrarium.spirit.common.recipes.PedestalRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeUtils {

    //get items and souls in the pedestals nearby
    public static Map<BlockPos, ItemStack> getPedestalItems(BlockPos blockPos, Level level) {
        Map<BlockPos, ItemStack> ingredients = new HashMap<>();
        AABB box = new AABB(blockPos).inflate(4,0,4);
        BlockPos.betweenClosedStream(box).forEach(pos -> {
            if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
                if (!pedestal.isEmpty()) {
                    ingredients.put(pos.immutable(), pedestal.getItem(0));
                }
            }
        });
        return ingredients;
    }

    public static Map<BlockPos, SoulStack> getPedestalSouls(BlockPos blockPos, Level level) {
        Map<BlockPos, SoulStack> ingredients = new HashMap<>();
        AABB box = new AABB(blockPos).inflate(4,0,4);
        BlockPos.betweenClosedStream(box).forEach(pos -> {
            if (level.getBlockEntity(pos) instanceof SoulBasinBlockEntity basinBlock) {
                if (!basinBlock.getContainer().isEmpty()) {
                    ingredients.put(pos.immutable(), basinBlock.getContainer().getSoulStack(0));
                }
            }
        });
        return ingredients;
    }

    public static boolean validatePedestals(Level level, PedestalRecipe<?> recipe, Map<BlockPos, ItemStack> ingredients, Map<BlockPos, SoulStack> soulIngredients, boolean consumeItems) {
        Map<BlockPos, ItemStack> markedIngredients = new HashMap<>();
        Map<BlockPos, SoulStack> markedSoulIngredients = new HashMap<>();

        List<Ingredient> recipeIngredients = new ArrayList<>(recipe.itemInputs());
        ingredients.forEach((pos, stack) -> {
            var item = recipeIngredients.stream().filter(ingredient -> ingredient.test(stack)).findFirst();
            item.ifPresent(ingredient -> {
                recipeIngredients.remove(ingredient);
                markedIngredients.put(pos.immutable(), stack);
            });
        });

        if (!recipeIngredients.isEmpty()) return false;

        List<SoulIngredient> recipeSoulIngredients = new ArrayList<>(recipe.entityInputs());
        soulIngredients.forEach((pos, stack) -> {
            var item = recipeSoulIngredients.stream().filter(ingredient -> ingredient.test(stack)).findFirst();
            item.ifPresent(ingredient -> {
                recipeSoulIngredients.remove(ingredient);
                markedSoulIngredients.put(pos.immutable(), stack);
            });
        });

        if (!recipeSoulIngredients.isEmpty()) return false;

        if (consumeItems) {
            markedIngredients.forEach((pos, stack) -> {
                if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
                    pedestal.getItem(0).shrink(1);
                    pedestal.update();
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 3, 0,0.1,0,0.01);
                    }
                }
            });
            markedSoulIngredients.forEach((pos, stack) -> {
                if (level.getBlockEntity(pos) instanceof SoulBasinBlockEntity basinBlock) {
                    SoulStack toRemove = basinBlock.getContainer().getSoulStack(0).copy();
                    toRemove.setAmount(1);
                    basinBlock.getContainer().extract(toRemove, InteractionMode.NO_TAKE_BACKSIES);
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 3, 0,0.1,0,0.01);
                    }
                }
            });
        }

        return true;
    }

    public static boolean validatePedestals(BlockPos pos, Level level, PedestalRecipe<?> recipe, boolean consumeItems) {
        return validatePedestals(level, recipe, getPedestalItems(pos, level), getPedestalSouls(pos, level), consumeItems);
    }
}
