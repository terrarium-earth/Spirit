package earth.terrarium.spirit.common.item;

import earth.terrarium.spirit.common.registry.SpiritBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;

public class ElementalCrystalItem extends Item {
    public ElementalCrystalItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.is(Blocks.SOUL_FIRE)) {
            level.playSound(player, blockPos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
            BlockState blockState2 = SpiritBlocks.RAGING_SOUL_FIRE.get().defaultBlockState();
            level.setBlock(blockPos, blockState2, Block.UPDATE_ALL_IMMEDIATE);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, blockPos);
            ItemStack itemStack = useOnContext.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockPos, itemStack);
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return InteractionResult.FAIL;
        }
    }
}
