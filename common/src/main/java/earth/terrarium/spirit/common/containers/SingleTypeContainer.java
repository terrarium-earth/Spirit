package earth.terrarium.spirit.common.containers;

import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.storage.SingleSoulStackContainer;
import earth.terrarium.spirit.api.utils.SoulStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;

public class SingleTypeContainer extends SingleSoulStackContainer {
    private static final String KEY = "Souls";

    private final int maxAmount;

    private SoulStack stack = SoulStack.empty();

    public SingleTypeContainer(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public SoulStack getSoulStack(int index) {
        return stack;
    }

    @Override
    public int insertIntoSlot(SoulStack soulStack, int slot, InteractionMode mode) {
        if(soulStack.getEntity() != null) {
            if(stack.isEmpty()) {
                soulStack.setAmount(Math.min(soulStack.getAmount(), maxCapacity()));
                stack = soulStack.copy();
                return soulStack.getAmount();
            } else {
                if(soulStack.getEntity() == stack.getEntity()) {
                    int insert = Math.min(soulStack.getAmount(), maxCapacity() - stack.getAmount());
                    stack.setAmount(stack.getAmount() + insert);
                    return insert;
                }
            }
        }
        return 0;
    }

    @Override
    public SoulStack extractFromSlot(SoulStack soulStack, int slot, InteractionMode mode) {
        if(!stack.isEmpty()) {
            if(soulStack.getEntity() == stack.getEntity()) {
                int extract = Math.min(soulStack.getAmount(), stack.getAmount());
                stack.setAmount(stack.getAmount() - extract);
                return new SoulStack(stack.getEntity(), extract);
            }
        }
        return SoulStack.empty();
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        if(stack != null && !stack.isEmpty()) {
            tag.put(KEY, stack.toTag(new CompoundTag()));
        } else {
            tag.remove(KEY);
        }
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        if(tag.contains(KEY)) {
            stack = SoulStack.fromTag(tag.getCompound(KEY));
        } else {
            stack = SoulStack.empty();
        }
    }

    @Override
    public int maxCapacity() {
        return maxAmount;
    }
}
