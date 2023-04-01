package earth.terrarium.spirit.common.abilities;

import earth.terrarium.spirit.api.armor_abilities.ArmorAbility;
import earth.terrarium.spirit.api.armor_abilities.ColorPalette;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;

public class StriderAbility extends ArmorAbility {
    @Override
    public void onEntityMove(ItemStack stack, LivingEntity livingEntity, Level level, BlockPos blockPos) {
        if (!livingEntity.isOnGround()) {
            return;
        }
        BlockState blockState = SpiritBlocks.FROSTED_LAVA.get().defaultBlockState();
        int j = 2;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-j, -1, -j), blockPos.offset(j, -1, j))) {
            BlockState blockState3;
            if (!blockPos2.closerToCenterThan(livingEntity.position(), j)) continue;
            mutableBlockPos.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
            BlockState blockState2 = level.getBlockState(mutableBlockPos);
            if (!blockState2.isAir() || (blockState3 = level.getBlockState(blockPos2)).getMaterial() != Material.LAVA || blockState3.getValue(LiquidBlock.LEVEL) != 0 || !blockState.canSurvive(level, blockPos2) || !level.isUnobstructed(blockState, blockPos2, CollisionContext.empty())) continue;
            level.setBlockAndUpdate(blockPos2, blockState);
            level.scheduleTick(blockPos2, SpiritBlocks.FROSTED_LAVA.get(), Mth.nextInt(livingEntity.getRandom(), 60, 120));
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0x821f1f);
    }
}
