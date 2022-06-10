package me.codexadrian.spirit.utils;

import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class RecipeUtils {

    public static final BlockPos[] CARDINAL_BLOCK_POSITIONS = new BlockPos[]{
            new BlockPos(0, -1, 1),
            new BlockPos(0, -1, -1),
            new BlockPos(1, -1, 0),
            new BlockPos(-1, -1, 0)
    };


    public static boolean checkMultiblock(BlockPos blockPos, Level level, BlockPredicate cardinalBlock, boolean breakBlock) {
        for (BlockPos cardinalPos : CARDINAL_BLOCK_POSITIONS) {
            if (!cardinalBlock.matches((ServerLevel) level, blockPos.offset(cardinalPos))) {
                return false;
            }
        }

        if (breakBlock) {
            if (!cardinalBlock.equals(BlockPredicate.ANY)) {
                for (BlockPos cardinalPos : CARDINAL_BLOCK_POSITIONS) {
                    level.destroyBlock(blockPos.offset(cardinalPos), false);
                }
            }

            level.destroyBlock(blockPos, false);
            level.destroyBlock(blockPos.below(), false);
        }

        return true;
    }
}
