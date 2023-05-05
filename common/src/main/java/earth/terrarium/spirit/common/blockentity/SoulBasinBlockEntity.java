package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.api.souls.SoulfulCreature;
import earth.terrarium.spirit.api.storage.BlockEntitySoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainingBlock;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.common.containers.SingleTypeContainer;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulBasinBlockEntity extends BlockEntity implements SoulContainingBlock {
    @Nullable
    public Entity entity;
    private BlockEntitySoulContainer soulContainer;

    @Nullable
    public TransmutationRecipe containedRecipe;
    public int burnTime = 0;
    public int age;

    public SoulBasinBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.SOUL_BASIN.get(), blockPos, blockState);
    }

    public void tick() {
        this.age = (this.age + 1) % Integer.MAX_VALUE;
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
        return soulContainer == null ? soulContainer = new BlockEntitySoulContainer(this, new SingleTypeContainer(16)) : soulContainer;
    }

    public Entity getOrCreateEntity() {
        if ((this.entity == null || this.entity.getType() != this.getContainer().getSoulStack(0).getEntity()) && this.hasLevel() && getContainer().getSoulStack(0).getEntity() != null) {
            this.entity = this.getContainer().getSoulStack(0).getEntity().create(getLevel());
            if (entity instanceof SoulfulCreature corrupted) corrupted.setIfSoulless(true);
        }
        return entity;
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void update() {
        this.setChanged();
        if (getLevel() != null)
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), net.minecraft.world.level.block.Block.UPDATE_ALL);
    }
}
