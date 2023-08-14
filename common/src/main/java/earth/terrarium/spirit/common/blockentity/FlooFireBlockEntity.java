package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FlooFireBlockEntity extends BlockEntity {
    public BlockPos targetPos;

    public FlooFireBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.FLOO_FIRE.get(), blockPos, blockState);
    }

    public void setTargetPos(BlockPos targetPos) {
        this.targetPos = targetPos;
    }

    public BlockPos getTargetPos() {
        return this.targetPos;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("TargetPos")) {
            this.targetPos = BlockPos.of(compoundTag.getLong("TargetPos"));
        }
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (this.targetPos != null) {
            compoundTag.putLong("TargetPos", this.targetPos.asLong());
        }
    }
}
