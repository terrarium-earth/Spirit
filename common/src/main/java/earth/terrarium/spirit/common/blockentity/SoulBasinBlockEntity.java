package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.botarium.common.fluid.base.BotariumFluidBlock;
import earth.terrarium.botarium.common.fluid.impl.SimpleFluidContainer;
import earth.terrarium.botarium.common.fluid.impl.WrappedBlockFluidContainer;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
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

public class SoulBasinBlockEntity extends BlockEntity implements BotariumFluidBlock<WrappedBlockFluidContainer> {
    private WrappedBlockFluidContainer fluidContainer;
    public int age;

    public SoulBasinBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.SOUL_BASIN.get(), blockPos, blockState);
    }

    public void tick() {
        this.age = (this.age + 1) % Integer.MAX_VALUE;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public WrappedBlockFluidContainer getFluidContainer() {
        if (fluidContainer == null) {
            fluidContainer = new WrappedBlockFluidContainer(this, new SimpleFluidContainer(FluidHooks.buckets(4), 1, (integer, fluidHolder) -> true));
        }
        return fluidContainer;
    }
}
