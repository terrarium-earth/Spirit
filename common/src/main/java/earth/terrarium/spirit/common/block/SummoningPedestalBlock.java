package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.common.blockentity.SummoningPedestalBlockEntity;
import earth.terrarium.spirit.common.recipes.SummoningRecipe;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummoningPedestalBlock extends BasePedestalBlock<SummoningRecipe> {
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 5, 0, 16, 10, 16),
            Block.box(3, 10, 3, 13, 11, 13),
            Block.box(4, 3, 4, 12, 5, 12),
            Block.box(2, 0, 2, 14, 3, 14)
    );

    public SummoningPedestalBlock(Properties properties) {
        super(SpiritRecipes.SUMMONING.get(), properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SummoningPedestalBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }
}
