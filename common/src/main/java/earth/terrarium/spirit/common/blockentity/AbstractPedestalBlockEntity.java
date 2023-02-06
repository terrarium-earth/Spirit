package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.api.storage.BlockEntitySoulContainer;
import earth.terrarium.spirit.common.recipes.PedestalRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.util.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class AbstractPedestalBlockEntity<T extends Recipe<?> & PedestalRecipe<?>> extends BlockEntity {
    @Nullable public Entity entity;
    private BlockEntitySoulContainer soulContainer;

    @Nullable
    public T containedRecipe;
    public RecipeType<T> recipeType;

    public int burnTime = 0;
    public int age;

    public AbstractPedestalBlockEntity(RecipeType<T> recipeType, BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.SUMMONING_PEDESTAL.get(), blockPos, blockState);
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
                for (int i = 0; i < 5; i++) {
                    if(this.burnTime < this.containedRecipe.duration() * .5) {
                        double percentage = 2 * this.burnTime / (double) this.containedRecipe.duration();
                        level.addParticle(ParticleTypes.SOUL,
                                blockPos.getX() + (3 * Math.sin(percentage * 2 * Math.PI)) + 0.5,
                                blockPos.getY() + 0.75,
                                blockPos.getZ() + (3 * Math.cos(percentage * 2 * Math.PI)) + 0.5,
                                0,
                                0,
                                0
                        );
                    } else {
                        double percentage = 2 * ((this.burnTime - this.containedRecipe.duration() * .5) / (double) this.containedRecipe.duration());
                        level.addParticle(ParticleTypes.SOUL,
                                blockPos.getX() + (3.0 * (1 - percentage) * Math.sin(percentage * 2 * Math.PI)) + 0.5,
                                blockPos.getY() + 0.75,
                                blockPos.getZ() + (3.0 * (1 - percentage) * Math.cos(percentage * 2 * Math.PI)) + 0.5,
                                0,
                                0,
                                0
                        );
                    }
                }
            } else if (RecipeUtils.validatePedestals(blockPos, level, containedRecipe, true)) {
                finishRecipe();
                /*
                Entity entity = this.containedRecipe.entityOutput().create(level);
                if (entity != null) {
                    entity.setPos(blockPos.getX() + 0.5, blockPos.getY() + 0.75, blockPos.getZ() + 0.5);
                    if(this.containedRecipe.outputNbt().isPresent()) entity.load(this.containedRecipe.outputNbt().get());
                    level.addFreshEntity(entity);
                    for (int i = 0; i < 10; i++) {
                        level.addParticle(ParticleTypes.SOUL, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
                    }
                    this.getContainer().extract(new SoulStack(this.getContainer().getSoulStack(0).getEntity(), containedRecipe.inputAmount()), InteractionMode.NO_TAKE_BACKSIES);
                } */
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
        containedRecipe = getLevel().getRecipeManager().getAllRecipesFor(recipeType).orElse(null);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
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
