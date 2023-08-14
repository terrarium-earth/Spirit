package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.common.blockentity.FlooFireBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class FlooFireBlock extends SoulFireBlock implements EntityBlock {
    public FlooFireBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FlooFireBlockEntity(blockPos, blockState);
    }
}
