package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.api.storage.BlockEntitySoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.common.containers.SingleTypeContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SummoningPedestalBlockEntity extends BlockEntity implements SoulContainingObject.Block {
    private BlockEntitySoulContainer soulContainer;

    public SummoningPedestalBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public void tick() {

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
    public @NotNull SoulContainer getContainer(BlockEntity ignored) {
        return soulContainer == null ? soulContainer = new BlockEntitySoulContainer(ignored, new SingleTypeContainer(16)) : soulContainer;
    }

    public @NotNull SoulContainer getContainer() {
        return getContainer(this);
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
}
