package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.blockentity.SoulBasinBlockEntity;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulBasinBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 9, 2, 14, 15, 14),
            Block.box(4, 7, 4, 12, 8, 12),
            Block.box(5, 3, 5, 11, 9, 11),
            Block.box(3, 0, 3, 13, 3, 13)
    );

    public SoulBasinBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SoulBasinBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (interactionHand != InteractionHand.OFF_HAND) {
            ItemStack stack = player.getItemInHand(interactionHand);
            if (level.getBlockEntity(blockPos) instanceof SoulBasinBlockEntity soulPedestal) {
                if (stack.getItem() instanceof SoulContainingObject.Item soulContainingObject) {
                    var soulContainer = soulContainingObject.getContainer(stack);
                    if (soulContainer == null) return InteractionResult.FAIL;
                    if (soulPedestal.getContainer().insert(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.SIMULATE) == 1 && soulContainer.extract(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.SIMULATE).getAmount() == 1) {
                        soulPedestal.getContainer().insert(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.NO_TAKE_BACKSIES);
                        soulContainer.extract(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.NO_TAKE_BACKSIES);
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    } else if (soulContainer.insert(new SoulStack(soulPedestal.getContainer().getSoulStack(0).getEntity(), 1), InteractionMode.SIMULATE) == 1) {
                        var soulStack = soulPedestal.getContainer().getSoulStack(0).copy();
                        SoulStack extract = soulPedestal.getContainer().extract(soulStack, InteractionMode.SIMULATE);
                        int inserted = soulContainer.insert(extract.copy(), InteractionMode.NO_TAKE_BACKSIES);
                        soulPedestal.getContainer().extract(new SoulStack(soulStack.getEntity(), inserted), InteractionMode.NO_TAKE_BACKSIES);
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    }
                } if (stack.getItem() instanceof SpawnEggItem spawnEggItem) {
                    var soulStack = new SoulStack(spawnEggItem.getType(stack.getTag()), 1);
                    if (soulPedestal.getContainer().insert(soulStack, InteractionMode.SIMULATE) == 1) {
                        soulPedestal.getContainer().insert(soulStack, InteractionMode.NO_TAKE_BACKSIES);
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SpiritBlockEntities.SOUL_BASIN.get(), (level1, blockPos, blockState1, blockEntity) -> blockEntity.tick());
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }
}
