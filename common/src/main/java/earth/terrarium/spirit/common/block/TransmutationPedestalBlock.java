package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.common.blockentity.TransmutationPedestalBlockEntity;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransmutationPedestalBlock extends BasePedestalBlock<TransmutationRecipe> {
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(4, 9, 4, 12, 10, 12),
            Block.box(2, 6, 2, 14, 9, 14),
            Block.box(4, 3, 4, 12, 6, 12),
            Block.box(2, 0, 2, 14, 3, 14)
    );

    public TransmutationPedestalBlock(Properties properties) {
        super(SpiritRecipes.TRANSMUTATION.get(), properties);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SpiritBlockEntities.TRANSMUTATION_PEDESTAL.get(), (level1, blockPos, blockState1, blockEntity) -> blockEntity.tick());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TransmutationPedestalBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }
}
