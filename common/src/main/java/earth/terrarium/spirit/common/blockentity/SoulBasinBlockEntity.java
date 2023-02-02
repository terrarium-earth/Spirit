package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.api.souls.SoulfulCreature;
import earth.terrarium.spirit.api.storage.BlockEntitySoulContainer;
import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.containers.SingleTypeContainer;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.util.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SoulBasinBlockEntity extends BlockEntity implements SoulContainingObject.Block {
    @Nullable public Entity entity;
    private BlockEntitySoulContainer soulContainer;

    @Nullable
    public TransmutationRecipe containedRecipe;
    public int burnTime = 0;
    public int age;

    public SoulBasinBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.SOUL_BASIN.get(), blockPos, blockState);
    }

    public void tick() {
        this.age = (this.age + 1) % Integer.MAX_VALUE;
        if (this.containedRecipe != null && this.level instanceof ServerLevel level) {
            BlockPos blockPos = getBlockPos();
            if (!RecipeUtils.validatePedestals(blockPos, level, this.containedRecipe.entityInput(), this.containedRecipe.inputAmount(), new ArrayList<>(this.containedRecipe.ingredients()), false)) {
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
            } else if (RecipeUtils.validatePedestals(blockPos, level, this.containedRecipe.entityInput(), this.containedRecipe.inputAmount(), new ArrayList<>(this.containedRecipe.ingredients()), true)) {
                Entity entity = this.containedRecipe.createEntity(level, blockPos.getX() + 0.5, blockPos.getY() + 0.75, blockPos.getZ() + 0.5);
                level.addFreshEntity(entity);
                level.sendParticles(ParticleTypes.SOUL, entity.getX(), entity.getY(), entity.getZ(), 10, 0, 0, 0, 0);
                this.getContainer().extract(new SoulStack(this.getContainer().getSoulStack(0).getEntity(), containedRecipe.inputAmount()), InteractionMode.NO_TAKE_BACKSIES);
                level.sendBlockUpdated(blockPos, getBlockState(), getBlockState(), net.minecraft.world.level.block.Block.UPDATE_ALL);
                this.setRecipe(null);
            }
            this.burnTime++;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        getContainer().serialize(compoundTag);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        getContainer().deserialize(compoundTag);
    }

    @Override
    public @NotNull SoulContainer getContainer(BlockEntity ignored) {
        return soulContainer == null ? soulContainer = new BlockEntitySoulContainer(ignored, new SingleTypeContainer(16)) : soulContainer;
    }

    public @NotNull SoulContainer getContainer() {
        return getContainer(this);
    }

    public Entity getOrCreateEntity() {
        if ((this.entity == null || this.entity.getType() != this.getContainer().getSoulStack(0).getEntity()) && this.hasLevel() && getContainer().getSoulStack(0).getEntity() != null) {
            this.entity = this.getContainer().getSoulStack(0).getEntity().create(getLevel());
            if (entity instanceof SoulfulCreature corrupted) corrupted.setIfSoulless(true);
        }
        return entity;
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

    public void setRecipe(@Nullable TransmutationRecipe recipe) {
        this.containedRecipe = recipe;
        if (recipe == null) burnTime = 0;
        this.setChanged();
    }
}
