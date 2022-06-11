package me.codexadrian.spirit.blocks.blockentity;

import me.codexadrian.spirit.SpiritRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SoulPedestalBlockEntity extends BlockEntity implements Container {

    ItemStack soulCrystal = ItemStack.EMPTY;

    public SoulPedestalBlockEntity(BlockPos $$1, BlockState $$2) {
        super(SpiritRegistry.SOUL_PEDESTAL_ENTITY.get(), $$1, $$2);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return soulCrystal.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return i == 0 ? soulCrystal : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return removeItemNoUpdate(i);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (i == 0) {
            ItemStack crystal = soulCrystal;
            soulCrystal = ItemStack.EMPTY;

            return crystal;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i == 0)
            soulCrystal = itemStack;
    }

    @Override
    public void clearContent() {
        soulCrystal = ItemStack.EMPTY;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("crystal")) {
            soulCrystal = ItemStack.of(compoundTag.getCompound("crystal"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!soulCrystal.isEmpty()) {
            compoundTag.put("crystal", soulCrystal.save(new CompoundTag()));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public boolean stillValid(Player player) {
        return worldPosition.distSqr(player.blockPosition()) <= 16;
    }

}
