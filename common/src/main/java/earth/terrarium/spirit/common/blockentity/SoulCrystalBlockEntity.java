package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.api.souls.SoulfulCreature;
import earth.terrarium.spirit.api.storage.BlockEntitySoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainingBlock;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.containers.SingleTypeContainer;
import earth.terrarium.spirit.common.containers.SoulCrystalBlockContainer;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulCrystalBlockEntity extends BlockEntity implements SoulContainingBlock {
    private SoulCrystalBlockContainer soulContainer;
    private Entity entity;
    public int age;

    public SoulCrystalBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.SOUL_CRYSTAL.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        getContainer().serialize(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        getContainer().deserialize(compoundTag);
    }

    @Override
    public @NotNull SoulContainer getContainer() {
        return soulContainer == null ? soulContainer = new SoulCrystalBlockContainer(this) : soulContainer;
    }

    public Entity getOrCreateEntity() {
        if ((this.entity == null || this.entity.getType() != this.getContainer().getSoulStack(0).getEntity()) && this.hasLevel() && getContainer().getSoulStack(0).getEntity() != null) {
            this.entity = this.getContainer().getSoulStack(0).getEntity().create(getLevel());
            if (entity instanceof SoulfulCreature corrupted) corrupted.setIfSoulless(true);
        }
        return entity;
    }
}
