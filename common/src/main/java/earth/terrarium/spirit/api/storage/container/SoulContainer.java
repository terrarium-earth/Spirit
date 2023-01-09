package earth.terrarium.spirit.api.storage.container;

import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.utils.SoulStack;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public interface SoulContainer {

    List<SoulStack> getSouls();

    SoulStack getSoulStack(int index);

    int insertIntoSlot(SoulStack soulStack, int slot, InteractionMode mode);

    int insert(SoulStack soulStack, InteractionMode mode);

    SoulStack extractFromSlot(SoulStack soulStack, int slot, InteractionMode mode);

    SoulStack extract(SoulStack soulStack, InteractionMode mode);

    CompoundTag serialize(CompoundTag tag);

    void deserialize(CompoundTag tag);

    int maxCapacity();

    default int slotCount() {
        return getSouls().size();
    }

    default boolean allowsInsertion() {
        return true;
    }

    default boolean allowsExtraction() {
        return true;
    }

    default boolean isEmpty() {
        return getSouls().stream().allMatch(SoulStack::isEmpty) || getSouls().isEmpty();
    }
}
