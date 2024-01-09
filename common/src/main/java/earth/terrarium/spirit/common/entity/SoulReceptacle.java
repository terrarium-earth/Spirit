package earth.terrarium.spirit.common.entity;

import com.mojang.datafixers.util.Either;
import earth.terrarium.spirit.api.rituals.components.RitualComponent;
import earth.terrarium.spirit.api.souls.SoulfulCreature;
import earth.terrarium.spirit.common.recipes.MultiblockRecipe;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SoulReceptacle extends Entity {
    private static final EntityDataAccessor<Integer> PROCESS_TIME = SynchedEntityData.defineId(SoulReceptacle.class, EntityDataSerializers.INT);

    Either<MultiblockRecipe, TransmutationRecipe> recipe;
    Map<BlockPos, RitualComponent<?>> components = new HashMap<>();
    BlockPos recipeOrigin = BlockPos.ZERO;
    ItemStack catalyst = ItemStack.EMPTY;

    public SoulReceptacle(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(PROCESS_TIME, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        setProcessTime(compoundTag.getInt("ProcessTime"));
        recipeOrigin = NbtUtils.readBlockPos(compoundTag.getCompound("RecipeOrigin"));
        catalyst = ItemStack.of(compoundTag.getCompound("Catalyst"));
        if (compoundTag.contains("Multiblock")) {
            ResourceLocation recipeKey = ResourceLocation.tryParse(compoundTag.getString("Multiblock"));
            if (recipeKey != null) {
                var multiblock = level().getRecipeManager().byKey(recipeKey).orElse(null);
                if (multiblock instanceof MultiblockRecipe multiblockRecipe) {
                    this.recipe = Either.left(multiblockRecipe);
                }
            }
        } else if (compoundTag.contains("Transmutation")) {
            ResourceLocation recipeKey = ResourceLocation.tryParse(compoundTag.getString("Transmutation"));
            if (recipeKey != null) {
                var transmutation = level().getRecipeManager().byKey(recipeKey).orElse(null);
                if (transmutation instanceof TransmutationRecipe transmutationRecipe) {
                    this.recipe = Either.right(transmutationRecipe);
                }
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("ProcessTime", getProcessTime());
        compoundTag.put("RecipeOrigin", NbtUtils.writeBlockPos(recipeOrigin));
        compoundTag.put("Catalyst", catalyst.save(new CompoundTag()));
        if (recipe != null) {
            if (recipe.left().isPresent()) {
                compoundTag.putString("Multiblock", recipe.left().get().getId().toString());
            } else if (recipe.right().isPresent()) {
                compoundTag.putString("Transmutation", recipe.right().get().getId().toString());
            }
        }
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public void setResult(ItemStack catalyst, MultiblockRecipe recipe, BlockPos origin) {
        this.recipe = Either.left(recipe);
        this.recipeOrigin = origin.immutable();
        this.catalyst = catalyst;
        setProcessTime(0);
    }

    public void setResult(ItemStack catalyst, TransmutationRecipe recipe, BlockPos origin) {
        this.recipe = Either.right(recipe);
        this.recipeOrigin = origin.immutable();
        this.catalyst = catalyst;
        setProcessTime(0);
    }

    public void setProcessTime(int processTime) {
        entityData.set(PROCESS_TIME, processTime);
    }

    public int getProcessTime() {
        return entityData.get(PROCESS_TIME);
    }

    public void incrementProcessTime() {
        setProcessTime(getProcessTime() + 1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            if (recipe != null) {
                if (recipe.left().isPresent()) {
                    MultiblockRecipe recipe = this.recipe.left().get();
                    if (recipe.multiblock().validateMultiblock(recipeOrigin, serverLevel, false)) {
                        if (getProcessTime() < recipe.duration()) {
                            incrementProcessTime();
                            if (getProcessTime() % 10 == 0) {
                                level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY() + 0.5, getZ(), 0, 0, 0);
                            }
                        } else {
                            recipe.result().onRitualComplete(level(), blockPosition(), catalyst);
                            level().addParticle(ParticleTypes.SOUL, getX(), getY() + 0.5, getZ(), 0, 0, 0);
                            discard();
                        }
                    } else {
                        Block.popResource(level(), recipeOrigin, catalyst);
                        discard();
                    }
                }
                else if (recipe.right().isPresent()) {
                    TransmutationRecipe recipe = this.recipe.right().get();
                    if (components.isEmpty() && !recipe.inputs().isEmpty()) {
                        for (RitualComponent<?> input : recipe.inputs()) {
                            boolean foundMatch = false;
                            List<BlockPos> list = BlockPos.betweenClosedStream(recipeOrigin.immutable().offset(-3, 0, -3), recipeOrigin.immutable().offset(3, 0, 3)).map(BlockPos::immutable).filter(pos -> !components.containsKey(pos)).toList();
                            for (BlockPos blockPos : list) {
                                if (input.matches(level(), blockPos, recipeOrigin)) {
                                    components.put(blockPos, input);
                                    foundMatch = true;
                                    break;
                                }
                            }
                            if (!foundMatch) {
                                Block.popResource(level(), recipeOrigin, catalyst);
                                discard();
                                return;
                            }
                        }
                    }
                    if (getProcessTime() < recipe.duration()) {
                        incrementProcessTime();
                        if (getProcessTime() % 10 == 0) {
                            if (!components.isEmpty() && !components.entrySet().stream().allMatch(entry -> entry.getValue().matches(level(), entry.getKey(), recipeOrigin))) {
                                Block.popResource(level(), recipeOrigin, catalyst);
                                discard();
                            }
                            level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getY() + 0.5, getZ(), 0, 0, 0);
                        }
                    } else {
                        if (components.isEmpty() || components.entrySet().stream().allMatch(entry -> entry.getValue().matches(level(), entry.getKey(), recipeOrigin))) {
                            recipe.result().onRitualComplete(level(), blockPosition(), catalyst);
                            components.forEach((blockPos, ritualComponent) -> ritualComponent.onRitualComplete(level(), blockPos, recipeOrigin));
                            level().addParticle(ParticleTypes.SOUL, getX(), getY() + 0.5, getZ(), 0, 0, 0);
                            discard();
                        } else {
                            Block.popResource(level(), recipeOrigin, catalyst);
                            discard();
                        }
                    }
                }
            } else {
                Block.popResource(level(), recipeOrigin, catalyst);
                discard();
            }
        }
    }

    public boolean verifyTransmutationRecipe() {
        return recipe != null && recipe.right().isPresent() && components.entrySet().stream().allMatch(entry -> entry.getValue().matches(level(), entry.getKey(), recipeOrigin));
    }
}
