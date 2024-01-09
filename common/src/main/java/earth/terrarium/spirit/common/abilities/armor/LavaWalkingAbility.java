package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

public class LavaWalkingAbility implements ArmorAbility {
    public static final BlockState FROSTED_LAVA = SpiritBlocks.FROSTED_LAVA.get().defaultBlockState();
    public static final int MIN_LIFETIME = 60;
    public static final int MAX_LIFETIME = 120;

    @Override
    public void onEntityMove(ItemStack stack, LivingEntity livingEntity, Level level, BlockPos pos) {
        if (!livingEntity.onGround()) {
            return;
        }
        int radius = 2;
        BlockPos.MutableBlockPos posAbove = new BlockPos.MutableBlockPos();
        for (BlockPos floorPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, -1, radius))) {
            if (!floorPos.closerToCenterThan(livingEntity.position(), radius)) continue;
            posAbove.set(floorPos.getX(), floorPos.getY() + 1, floorPos.getZ());
            BlockState stateAbove = level.getBlockState(posAbove);
            BlockState floorState = level.getBlockState(floorPos);
            if (!stateAbove.isAir()) continue;
            if (!floorState.is(Blocks.LAVA) || floorState.getValue(LiquidBlock.LEVEL) != 0) continue;
            if (!FROSTED_LAVA.canSurvive(level, floorPos)) continue;
            if (!level.isUnobstructed(FROSTED_LAVA, floorPos, CollisionContext.empty())) continue;
            level.setBlockAndUpdate(floorPos, FROSTED_LAVA);
            level.scheduleTick(floorPos, FROSTED_LAVA.getBlock(), Mth.nextInt(livingEntity.getRandom(), MIN_LIFETIME, MAX_LIFETIME));
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0x821f1f);
    }
}
