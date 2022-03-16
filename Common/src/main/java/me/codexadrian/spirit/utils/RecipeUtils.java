package me.codexadrian.spirit.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class RecipeUtils {

    public static final BlockPos[] WARPED_WART_POSITIONS = new BlockPos[] {
            new BlockPos(0, -1,1),
            new BlockPos(0,-1,-1),
            new BlockPos(1, -1,0),
            new BlockPos(-1,-1,0)
    };

    public static boolean checkMultiblock(BlockPos blockPos, Level level) {
        for(BlockPos glassPos : WARPED_WART_POSITIONS) {
            if (!level.getBlockState(blockPos.offset(glassPos)).is(Blocks.WARPED_WART_BLOCK)) {
                return false;
            }
        }

        for(BlockPos glassPos : WARPED_WART_POSITIONS) {
            level.destroyBlock(blockPos.offset(glassPos), false);
        }

        return true;
    }
}
