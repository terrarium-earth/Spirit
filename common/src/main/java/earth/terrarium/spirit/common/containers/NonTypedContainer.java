package earth.terrarium.spirit.common.containers;

import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.storage.SingleSoulStackContainer;
import earth.terrarium.spirit.api.utils.SoulStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;

public class NonTypedContainer extends SingleSoulStackContainer {
    private final SoulStack crudeSoul = new SoulStack(null, 0);
    private final String KEY = "Souls";
    private final int maxAmount;

    public NonTypedContainer(int maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public SoulStack getSoulStack(int index) {
        return crudeSoul;
    }

    @Override
    public int insertIntoSlot(SoulStack stack, int slot, InteractionMode mode) {
        int stored = crudeSoul.getAmount();
        int insert = Mth.clamp(stack.getAmount(), 0, maxCapacity() - stored);
        if(mode == InteractionMode.SIMULATE) return insert;
        crudeSoul.setAmount(stored + insert);
        return insert;
    }

    @Override
    public SoulStack extractFromSlot(SoulStack stack, int slot, InteractionMode mode) {
        int stored = crudeSoul.getAmount();
        int extracted = Mth.clamp(stack.getAmount(), 0, stored);
        SoulStack extractedStack = new SoulStack(crudeSoul.getEntity(), extracted);
        if(mode == InteractionMode.SIMULATE) return extractedStack;
        crudeSoul.setAmount(stored - extracted);
        return extractedStack;
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        tag.putInt(KEY, crudeSoul.getAmount());
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        crudeSoul.setAmount(tag.getInt(KEY));
    }

    @Override
    public int maxCapacity() {
        return maxAmount;
    }
}
