package earth.terrarium.spirit.api.storage;

import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.utils.SoulStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public record BlockEntitySoulContainer(BlockEntity entity, SoulContainer container) implements SoulContainer {
    @Override
    public List<SoulStack> getSouls() {
        return container.getSouls();
    }

    @Override
    public SoulStack getSoulStack(int index) {
        return container.getSoulStack(index);
    }

    @Override
    public int insertIntoSlot(SoulStack soulStack, int slot, InteractionMode mode) {
        int insert = container.insertIntoSlot(soulStack, slot, mode);
        if (mode == InteractionMode.NO_TAKE_BACKSIES) update();
        return insert;
    }

    @Override
    public int insert(SoulStack soulStack, InteractionMode mode) {
        int insert = container.insert(soulStack, mode);
        if (mode == InteractionMode.NO_TAKE_BACKSIES) update();
        return insert;
    }

    @Override
    public SoulStack extractFromSlot(SoulStack soulStack, int slot, InteractionMode mode) {
        SoulStack extract = container.extractFromSlot(soulStack, slot, mode);
        if (mode == InteractionMode.NO_TAKE_BACKSIES) update();
        return extract;
    }

    @Override
    public SoulStack extract(SoulStack soulStack, InteractionMode mode) {
        SoulStack extract = container.extract(soulStack, mode);
        if (mode == InteractionMode.NO_TAKE_BACKSIES) update();
        return extract;
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        return container.serialize(tag);
    }

    @Override
    public void deserialize(CompoundTag tag) {
        container.deserialize(tag);
    }

    @Override
    public int maxCapacity() {
        return container.maxCapacity();
    }

    private void update() {
        entity.setChanged();
        entity.getLevel().sendBlockUpdated(entity.getBlockPos(), entity.getBlockState(), entity.getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
    }
}
