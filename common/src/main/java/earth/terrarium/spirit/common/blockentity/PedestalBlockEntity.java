package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PedestalBlockEntity extends BlockEntity implements WorldlyContainer {

    NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    public int age;

    public PedestalBlockEntity(BlockPos $$1, BlockState $$2) {
        super(SpiritBlockEntities.PEDESTAL.get(), $$1, $$2);
    }

    public static void tick(Level level1, BlockPos blockPos, BlockState blockState1, BlockEntity blockEntity) {
        if (blockEntity instanceof PedestalBlockEntity soulPedestal) {
            soulPedestal.age = (soulPedestal.age + 1) % Integer.MAX_VALUE;
        }
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return inventory.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return inventory.get(i);
    }

    public void update() {
        this.setChanged();
        if(getLevel() != null) getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(inventory, i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(inventory, i);
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemStack) {
        inventory.set(i, itemStack);
    }

    @Override
    public void clearContent() {
        inventory.clear();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        inventory = NonNullList.withSize(1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, inventory);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, inventory);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public boolean stillValid(Player player) {
        return worldPosition.distSqr(player.blockPosition()) <= 16;
    }

    @Override
    public int[] getSlotsForFace(@NotNull Direction direction) {
        return new int[]{0};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, @NotNull ItemStack itemStack, @Nullable Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        return true;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
