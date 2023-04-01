package earth.terrarium.spirit.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FrostedLavaBlock extends Block {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final int NEIGHBORS_TO_AGE = 4;
    private static final int NEIGHBORS_TO_MELT = 2;

    public FrostedLavaBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        this.tick(blockState, serverLevel, blockPos, randomSource);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        if ((randomSource.nextInt(MAX_AGE) == 0 || this.fewerNeigboursThan(serverLevel, blockPos, NEIGHBORS_TO_AGE)) && this.slightlyMelt(blockState, serverLevel, blockPos)) {
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                mutableBlockPos.setWithOffset(blockPos, direction);
                BlockState blockState2 = serverLevel.getBlockState(mutableBlockPos);
                if (!blockState2.is(this) || this.slightlyMelt(blockState2, serverLevel, mutableBlockPos)) continue;
                serverLevel.scheduleTick(mutableBlockPos, this, Mth.nextInt(randomSource, 20, 40));
            }
            return;
        }
        serverLevel.scheduleTick(blockPos, this, Mth.nextInt(randomSource, 20, 40));
    }

    private boolean slightlyMelt(BlockState blockState, Level level, BlockPos blockPos) {
        int i = blockState.getValue(AGE);
        if (i < MAX_AGE) {
            level.setBlock(blockPos, blockState.setValue(AGE, i + 1), Block.UPDATE_CLIENTS);
            return false;
        }
        this.melt(blockState, level, blockPos);
        return true;
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (block.defaultBlockState().is(this) && this.fewerNeigboursThan(level, blockPos, NEIGHBORS_TO_MELT)) {
            this.melt(blockState, level, blockPos);
        }
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
    }

    private boolean fewerNeigboursThan(BlockGetter blockGetter, BlockPos blockPos, int i) {
        int j = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            mutableBlockPos.setWithOffset(blockPos, direction);
            if (!blockGetter.getBlockState(mutableBlockPos).is(this) || ++j < i) continue;
            return false;
        }
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return ItemStack.EMPTY;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity, ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) == 0) {
            if (level.dimensionType().ultraWarm()) {
                level.removeBlock(blockPos, false);
                return;
            }
            Material material = level.getBlockState(blockPos.below()).getMaterial();
            if (material.blocksMotion() || material.isLiquid()) {
                level.setBlockAndUpdate(blockPos, Blocks.LAVA.defaultBlockState());
            }
        }
    }

    protected void melt(BlockState blockState, Level level, BlockPos blockPos) {
        level.setBlockAndUpdate(blockPos, Blocks.LAVA.defaultBlockState());
        level.neighborChanged(blockPos, Blocks.LAVA, blockPos);
    }

    @Override
    public @NotNull PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.IGNORE;
    }
}
