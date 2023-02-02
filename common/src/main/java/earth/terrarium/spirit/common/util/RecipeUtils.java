package earth.terrarium.spirit.common.util;

import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.storage.util.SoulIngredient;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.blockentity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeUtils {

    public static final BlockPos[] CARDINAL_BLOCK_POSITIONS = new BlockPos[]{
            new BlockPos(0, 0, 3),
            new BlockPos(0, 0, -3),
            new BlockPos(3, 0, 0),
            new BlockPos(-3, 0, 0),
            new BlockPos(2, 0, 2),
            new BlockPos(-2, 0, 2),
            new BlockPos(2, 0, -2),
            new BlockPos(-2, 0, -2)
    };


    public static boolean validatePedestals(BlockPos blockPos, Level level, SoulIngredient soulIngredient, int soulIngredientAmount, List<Ingredient> recipeIngredients, boolean consumeItems) {
        Map<BlockPos, ItemStack> ingredients = new HashMap<>();
        Map<BlockPos, ItemStack> markedIngredients = new HashMap<>();
        for (BlockPos cardinalPos : CARDINAL_BLOCK_POSITIONS) {
            BlockPos offset = blockPos.offset(cardinalPos).immutable();
            if(level.getBlockEntity(offset) instanceof PedestalBlockEntity pedestal) {
                if(!pedestal.isEmpty()) {
                    ingredients.put(offset, pedestal.getItem(0));
                }
            }
        }

        BlockEntity centerPedestal = level.getBlockEntity(blockPos);
        if (centerPedestal instanceof SoulContainingObject.Block block) {
            SoulStack soulStack = block.getContainer(centerPedestal).getSoulStack(0);
            if (!soulIngredient.test(soulStack) && soulStack.getAmount() <= soulIngredientAmount) return false;
        }

        ingredients.forEach((pos, stack) -> {
            var item = recipeIngredients.stream().filter(ingredient -> ingredient.test(stack)).findFirst();
            item.ifPresent(ingredient -> {
                recipeIngredients.remove(ingredient);
                markedIngredients.put(pos, stack);
            });
        });

        if(!recipeIngredients.isEmpty()) return false;

        if (consumeItems) {
            markedIngredients.forEach((pos, stack) -> {
                if(level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
                    pedestal.getItem(0).shrink(1);
                    pedestal.update();
                }
            });
        }

        return true;
    }
}
