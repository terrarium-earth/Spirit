package earth.terrarium.spirit.common.containers;

import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.utils.SoulStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SoulCrystalContainer implements SoulContainer {
    private EntityType<?> type = null;
    private final ItemStack stack;

    public SoulCrystalContainer(ItemStack stack) {
        this.stack = stack;
        deserialize(stack.getOrCreateTag());
    }

    @Override
    public List<SoulStack> getSouls() {
        return List.of(new SoulStack(type, stack.getCount()));
    }

    @Override
    public SoulStack getSoulStack(int index) {
        return new SoulStack(type, stack.getCount());
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
        SoulStack extract = soulStack.getEntity() == null || soulStack.getEntity().equals(type) ? new SoulStack(type, Math.min(soulStack.getAmount(), stack.getCount())) : SoulStack.empty();
        if (mode == InteractionMode.NO_TAKE_BACKSIES) {
            stack.setCount(stack.getCount() - extract.getAmount());
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
        return stack.getMaxStackSize();
    }

    public void update() {
    }

    @Override
    public boolean isEmpty() {
        return type == null;
    }
}

