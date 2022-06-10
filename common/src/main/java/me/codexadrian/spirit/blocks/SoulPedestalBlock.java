package me.codexadrian.spirit.blocks;

import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.blocks.blockentity.SoulCageBlockEntity;
import me.codexadrian.spirit.blocks.blockentity.SoulPedestalBlockEntity;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulPedestalBlock extends BaseEntityBlock {

    public SoulPedestalBlock(Properties $$0) {
        super($$0);
    }

    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (interactionHand != InteractionHand.OFF_HAND) {
            ItemStack itemStack = player.getMainHandItem();
            if (level.getBlockEntity(blockPos) instanceof SoulPedestalBlockEntity soulPedestal) {
                if (soulPedestal.isEmpty()) {
                    if ((itemStack.getItem() == SpiritRegistry.SOUL_CRYSTAL.get() || itemStack.getItem() == SpiritRegistry.CRUDE_SOUL_CRYSTAL.get())) {

                        soulPedestal.setItem(0, itemStack);

                        if (!player.getAbilities().instabuild) {
                            itemStack.shrink(1);
                        }

                        soulPedestal.setChanged();
                        level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
                        return InteractionResult.SUCCESS;
                    }
                } else if (player.isShiftKeyDown()) {
                    ItemStack soulCrystal = soulPedestal.removeItemNoUpdate(0);
                    if (itemStack.isEmpty()) {
                        player.setItemInHand(interactionHand, soulCrystal);
                    } else if (!player.addItem(soulCrystal)) {
                        player.drop(soulCrystal, false);
                    }

                    soulPedestal.setChanged();
                    level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SoulPedestalBlockEntity(blockPos, blockState);
    }
}
