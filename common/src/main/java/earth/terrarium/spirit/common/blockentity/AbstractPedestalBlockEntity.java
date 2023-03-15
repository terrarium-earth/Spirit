package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.common.recipes.PedestalRecipe;
import earth.terrarium.spirit.common.util.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractPedestalBlockEntity<T extends PedestalRecipe<?>> extends BlockEntity {
    @Nullable
    public Entity entity;

    @Nullable
    public T containedRecipe;
    public RecipeType<T> recipeType;

    public int burnTime = 0;
    public int age;

    public AbstractPedestalBlockEntity(BlockEntityType<?> entity, BlockPos blockPos, BlockState blockState, RecipeType<T> recipeType) {
        super(entity, blockPos, blockState);
        this.recipeType = recipeType;
    }

    public void tick() {
        this.age = (this.age + 1) % Integer.MAX_VALUE;
        if (this.containedRecipe != null && level != null) {
            BlockPos blockPos = getBlockPos();
            if (!RecipeUtils.validatePedestals(blockPos, level, containedRecipe, false)) {
                this.setRecipe(null);
                return;
            }
            if (this.burnTime < this.containedRecipe.duration()) {
                double percentage = this.burnTime / (double) this.containedRecipe.duration();
                for (double increment = 0; increment < percentage; increment += 0.1) {
                    level.addParticle(ParticleTypes.SOUL,
                            blockPos.getX() + 0.75 * Math.sin(increment * 2 * Math.PI) + 0.5,
                            blockPos.getY() + 1.25,
                            blockPos.getZ() + 0.75 * Math.cos(increment * 2 * Math.PI) + 0.5,
                            0,
                            0,
                            0
                    );
                }
            } else if (RecipeUtils.validatePedestals(blockPos, level, containedRecipe, true)) {
                finishRecipe();
                level.sendBlockUpdated(blockPos, getBlockState(), getBlockState(), net.minecraft.world.level.block.Block.UPDATE_ALL);
                this.setRecipe(null);
            }
            this.burnTime++;
        }
    }

    public abstract void finishRecipe();

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (containedRecipe != null) {
            compoundTag.putString("Recipe", containedRecipe.getId().toString());
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("Recipe")) {
            containedRecipe = (T) getLevel().getRecipeManager().byKey(new ResourceLocation(compoundTag.getString("Recipe"))).orElse(null);
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setRecipe(@Nullable T recipe) {
        this.containedRecipe = recipe;
        if (recipe == null) burnTime = 0;
        this.setChanged();
    }
}
