package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.common.blockentity.SoulCageBlockEntity;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SoulCageBlock extends BaseEntityBlock {
    public SoulCageBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide && interactionHand == InteractionHand.MAIN_HAND) {
            //insert item
            ItemStack stack = player.getItemInHand(interactionHand);
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof SoulCageBlockEntity cage) {
                if (cage.canInsertCrystal(stack)) {
                    cage.setItem(0, stack);
                    player.setItemInHand(interactionHand, ItemStack.EMPTY);
                    cage.update();
                    return InteractionResult.SUCCESS;
                } else if (stack.isEmpty()) {
                    player.setItemInHand(interactionHand, cage.removeItemNoUpdate(0));
                    cage.update();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SoulCageBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SpiritBlockEntities.SOUL_CAGE.get(), (level1, blockPos, blockState1, blockEntity) -> ((SoulCageBlockEntity) blockEntity).tick());
    }
}
