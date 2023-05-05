package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.common.recipes.ArmorInfusionRecipe;
import earth.terrarium.spirit.common.recipes.InfusionRecipe;
import earth.terrarium.spirit.common.recipes.ToolInfusionRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.util.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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

import java.util.Optional;

public class InfusionTableBlockEntity extends BlockEntity implements WorldlyContainer {

    NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    InfusionRecipe<?> recipe;
    public int processTime;
    public int age;

    public InfusionTableBlockEntity(BlockPos blockPos, BlockState state) {
        super(SpiritBlockEntities.INFUSION_PEDESTAL.get(), blockPos, state);
    }

    public static void tick(Level level1, BlockPos blockPos, BlockState blockState1, BlockEntity blockEntity) {
        if (blockEntity instanceof InfusionTableBlockEntity soulPedestal) {
            soulPedestal.age = (soulPedestal.age + 1) % Integer.MAX_VALUE;
        }
        if (level1 instanceof ServerLevel serverLevel) {
            if (blockEntity instanceof InfusionTableBlockEntity infusionTable && infusionTable.recipe != null) {
                if (infusionTable.processTime < infusionTable.recipe.duration()) {
                    if (infusionTable.processTime % 10 == 0 && !RecipeUtils.validatePedestals(blockPos, level1, infusionTable.recipe, false)) {
                        infusionTable.recipe = null;
                        infusionTable.processTime = 0;
                        infusionTable.update();
                    }
                    infusionTable.processTime++;
                    double progress = ((double) infusionTable.processTime / (double) infusionTable.recipe.duration());
                    for (double i = progress * 2 * Math.PI; i < 2 * Math.PI + progress * 2 * Math.PI; i += Math.PI / 2) {
                        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, blockPos.getX() + 0.5 + (1 - progress) * 2 * Math.cos(i), blockPos.getY() + 1, blockPos.getZ() + 0.5 + (1 - progress) * 2 * Math.sin(i), 1, 0, 0, 0, 0);
                    }
                } else if (RecipeUtils.validatePedestals(blockPos, level1, infusionTable.recipe, true)) {
                    infusionTable.setItem(0, infusionTable.recipe.getInfusionResult(infusionTable.inventory.get(0)));
                    serverLevel.sendParticles(ParticleTypes.SOUL, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.1, 0.1, 0.1, 0.1);
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 10, 0.1, 0.1, 0.1, 0.1);
                    infusionTable.processTime = 0;
                    infusionTable.recipe = null;
                    infusionTable.update();
                } else {
                    infusionTable.recipe = null;
                    infusionTable.processTime = 0;
                    infusionTable.update();
                }
            }
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
    public @NotNull ItemStack getItem(int i) {
        return inventory.get(i);
    }

    public void update() {
        this.setChanged();
        if(getLevel() != null) getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(inventory, i, j);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
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
        ResourceLocation recipeId = new ResourceLocation(compoundTag.getString("Recipe"));
        if (level != null && compoundTag.contains("Recipe")) {
            recipe = Optional.<InfusionRecipe<?>>ofNullable(ToolInfusionRecipe.getRecipeFromId(recipeId, level.getRecipeManager())).orElse(ArmorInfusionRecipe.getRecipeFromId(recipeId, level.getRecipeManager()));
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, inventory);
        if (recipe != null) {
            compoundTag.putString("Recipe", recipe.getId().toString());
        } else {
            compoundTag.remove("Recipe");
        }
    }

    public void setRecipe(InfusionRecipe<?> recipe) {
        this.recipe = recipe;
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
