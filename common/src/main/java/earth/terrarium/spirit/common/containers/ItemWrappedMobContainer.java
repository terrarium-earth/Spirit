package earth.terrarium.spirit.common.containers;

import earth.terrarium.spirit.api.souls.InteractionMode;
import earth.terrarium.spirit.api.souls.base.MobContainer;
import earth.terrarium.spirit.api.souls.stack.SoulStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ItemWrappedMobContainer(ItemStack stack, MobContainer container) implements MobContainer {

    public ItemWrappedMobContainer {
        container.deserialize(stack.getOrCreateTag());
    }

    @Override
    public boolean insertMob(LivingEntity mob) {
        boolean b = container.insertMob(mob);
        if (b) update();
        return b;
    }

    @Nullable
    @Override
    public LivingEntity extractMob(Level level) {
        LivingEntity entity = container.extractMob(level);
        if (entity != null) update();
        return entity;
    }

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
        SoulStack soulStack1 = container.extractFromSlot(soulStack, slot, mode);
        if (mode == InteractionMode.NO_TAKE_BACKSIES) update();
        return soulStack1;
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

    public void update() {
        container.serialize(stack.getOrCreateTag());
    }
}
