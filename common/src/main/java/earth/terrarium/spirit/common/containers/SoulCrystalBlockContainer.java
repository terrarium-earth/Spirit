package earth.terrarium.spirit.common.containers;

import earth.terrarium.spirit.api.souls.InteractionMode;
import earth.terrarium.spirit.api.souls.Updatable;
import earth.terrarium.spirit.api.souls.base.SoulContainer;
import earth.terrarium.spirit.api.souls.stack.SoulStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class SoulCrystalBlockContainer implements SoulContainer, Updatable {
    private EntityType<?> type = null;
    private final BlockEntity blockEntity;

    public SoulCrystalBlockContainer(BlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public List<SoulStack> getSouls() {
        return List.of(type == null ? SoulStack.empty() : new SoulStack(type, 1));
    }

    @Override
    public SoulStack getSoulStack(int index) {
        return type == null ? SoulStack.empty() : new SoulStack(type, 1);
    }

    @Override
    public int insertIntoSlot(SoulStack soulStack, int slot, InteractionMode mode) {
        return 0;
    }

    @Override
    public int insert(SoulStack soulStack, InteractionMode mode) {
        return 0;
    }

    @Override
    public SoulStack extractFromSlot(SoulStack soulStack, int slot, InteractionMode mode) {
        SoulStack extract = soulStack.getEntity() == null || soulStack.getEntity().equals(type) ? new SoulStack(type, 1) : SoulStack.empty();
        if (mode == InteractionMode.NO_TAKE_BACKSIES && blockEntity.hasLevel()) {
            blockEntity.getLevel().destroyBlock(blockEntity.getBlockPos(), false);
        };
        return extract;
    }

    @Override
    public SoulStack extract(SoulStack soulStack, InteractionMode mode) {
        return extractFromSlot(soulStack, 0, mode);
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        if (type != null) tag.putString("Entity", EntityType.getKey(type).toString());
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        type = tag.getString("Entity").isEmpty() ? null : EntityType.byString(tag.getString("Entity")).orElse(null);
    }

    @Override
    public int maxCapacity() {
        return 1;
    }

    @Override
    public void update() {
        if (blockEntity.hasLevel()) {
            blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), Block.UPDATE_ALL);
        }
    }
}

