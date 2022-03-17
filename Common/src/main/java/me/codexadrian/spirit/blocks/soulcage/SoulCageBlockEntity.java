package me.codexadrian.spirit.blocks.soulcage;

import me.codexadrian.spirit.SpiritRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SoulCageBlockEntity extends BlockEntity implements Container {

    EntityType<?> type;
    private ItemStack divineCrystal = ItemStack.EMPTY;

    public Entity entity;

    private final SoulCageSpawner enabledSpawner = new SoulCageSpawner(this);

    public SoulCageBlockEntity(BlockPos pos, BlockState state) {
        super(SpiritRegistry.SOUL_CAGE_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SoulCageBlockEntity blockEntity) {
        if (blockEntity.hasLevel() && !blockEntity.isEmpty()) {
            blockEntity.enabledSpawner.tick();
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean triggerEvent(int i, int j) {
        return this.enabledSpawner.onEventTriggered(i) || super.triggerEvent(i, j);
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return divineCrystal.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return i == 0 ? divineCrystal : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return removeItemNoUpdate(i);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (i == 0) {
            ItemStack sword = divineCrystal;
            divineCrystal = ItemStack.EMPTY;

            return sword;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i == 0)
            divineCrystal = itemStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return worldPosition.distSqr(player.blockPosition()) <= 16;
    }

    @Override
    public void clearContent() {
        divineCrystal = ItemStack.EMPTY;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        type = null;
        if (compoundTag.contains("crystal")) {
            divineCrystal = ItemStack.of(compoundTag.getCompound("crystal"));
            setType();
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (!divineCrystal.isEmpty()) {
            compoundTag.put("crystal", divineCrystal.save(new CompoundTag()));
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    public void setType() {
        if (divineCrystal.hasTag()) {
            type = Registry.ENTITY_TYPE.get(new ResourceLocation(divineCrystal.getTag().getCompound("StoredEntity").getString("Type")));
        } else {
            type = null;
        }
    }

    public SoulCageSpawner getSpawner() {
        return this.enabledSpawner;
    }
}
