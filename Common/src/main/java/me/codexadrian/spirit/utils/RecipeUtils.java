package me.codexadrian.spirit.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class RecipeUtils {

    public static final BlockPos[] CARDINAL_BLOCK_POSITIONS = new BlockPos[] {
            new BlockPos(0, -1,1),
            new BlockPos(0,-1,-1),
            new BlockPos(1, -1,0),
            new BlockPos(-1,-1,0)
    };


    public static boolean checkMultiblock(BlockPos blockPos, Level level, Block cardinalBlock) {
        for(BlockPos lapisPos : CARDINAL_BLOCK_POSITIONS) {
            if (!level.getBlockState(blockPos.offset(lapisPos)).is(cardinalBlock)) {
                return false;
            }
        }

        for(BlockPos lapisPos : CARDINAL_BLOCK_POSITIONS) {
            level.destroyBlock(blockPos.offset(lapisPos), false);
        }


        level.destroyBlock(blockPos, false);
        level.destroyBlock(blockPos.below(), false);


        return true;
    }
}
