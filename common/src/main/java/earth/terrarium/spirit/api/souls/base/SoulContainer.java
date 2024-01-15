package earth.terrarium.spirit.api.souls.base;

import earth.terrarium.spirit.api.souls.InteractionMode;
import earth.terrarium.spirit.api.souls.SoulApi;
import earth.terrarium.spirit.api.souls.Updatable;
import earth.terrarium.spirit.api.souls.stack.SoulStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static earth.terrarium.spirit.api.souls.SoulApi.getSoulContainingBlock;

public interface SoulContainer {

    @Nullable
    static SoulContainer of(Level level, BlockPos pos, @Nullable BlockState state, @Nullable BlockEntity entity, @Nullable Direction direction) {
        SoulContainingBlock<?> getter = state == null ? null : SoulApi.getSoulContainingBlock(state.getBlock());

        if (getter == null && entity != null) {
            getter = SoulApi.getSoulContainingBlock(entity.getType());

            if (getter == null && entity instanceof SoulContainingBlock<?> energyGetter) {
                getter = energyGetter;
            }
        }

        if (getter == null) {
            return null;
        }

        return getter.getContainer(level, pos, state, entity, direction);
    }

    @Nullable
    static SoulContainer of(Level level, BlockPos pos, @Nullable Direction direction) {
        return of(level, pos, null, null, direction);
    }

    @Nullable
    static SoulContainer of(ItemStack stack) {
        SoulContainingItem<?> getter = SoulApi.getSoulContainingItem(stack.getItem());

        if (getter == null && stack.getItem() instanceof SoulContainingItem<?> energyGetter) {
            getter = energyGetter;
        }

        if (getter == null) {
            return null;
        }

        return getter.getContainer(stack);
    }

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
