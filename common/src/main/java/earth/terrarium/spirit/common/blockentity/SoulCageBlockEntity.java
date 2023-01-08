package earth.terrarium.spirit.common.blockentity;

import com.ibm.icu.impl.Pair;
import com.teamresourceful.resourcefullib.common.collections.WeightedCollection;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.souls.SoulContainingCreature;
import earth.terrarium.spirit.api.souls.Tier;
import earth.terrarium.spirit.api.storage.SoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.storage.Tierable;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SoulCageBlockEntity extends BlockEntity implements WorldlyContainer {
    private int work = 0;
    private ItemStack crystalStorage;
    private Pair<String, Entity> entityStorage;

    public SoulCageBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.SOUL_CAGE.get(), blockPos, blockState);
    }

    public void tick() {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (crystalStorage != null && !crystalStorage.isEmpty()) {
            if (crystalStorage.getItem() instanceof Tierable tierable) {
                Tier tier = tierable.getTier(crystalStorage);
                if (tier != null) {
                    if (work < tier.workTime()) {
                        double fraction = (double) work / tier.workTime();
                        for (double i = 0; i < 2 * Math.PI; i += .2) {
                            double size = tier.spawnRange() * fraction;
                            double x = this.getBlockPos().getX() + 0.5;
                            double z = this.getBlockPos().getZ() + 0.5;
                            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x + size * Math.cos(i), this.getBlockPos().getY() + 0.5, z + size * Math.sin(i), 1, 0, 0, 0, 0);
                        }
                        work++;
                    } else {
                        work = 0;
                        if (crystalStorage.getItem() instanceof SoulContainingObject.Item item) {
                            SoulContainer container = item.getContainer(crystalStorage);
                            if (container != null) {
                                if (container.slotCount() > 0) {
                                    WeightedCollection<EntityType<?>> collection = new WeightedCollection<>();
                                    for (int i = 0; i < container.slotCount(); i++) {
                                        SoulStack soulStack = container.getSoulStack(i);
                                        if (soulStack != null && soulStack.getEntity() != null && !soulStack.getEntity().is(Spirit.BLACKLISTED_TAG)) {
                                            collection.add(soulStack.getAmount(), soulStack.getEntity());
                                        }
                                    }
                                    if (!collection.isEmpty()) {
                                        for (double i = 0; i < 2 * Math.PI; i += Math.PI / tier.spawnCount()) {
                                            spawnMob(serverLevel, tier, i, collection::next);
                                        }
                                    }
                                } else {
                                    SoulStack stack = container.getSoulStack(0);
                                    for (double i = 0; i < 2 * Math.PI; i += Math.PI / tier.spawnCount()) {
                                        spawnMob(serverLevel, tier, i, stack::getEntity);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void spawnMob(ServerLevel serverLevel, Tier tier, double i, Supplier<EntityType<?>> typeGetter) {
        double size = tier.spawnRange();
        double x = this.getBlockPos().getX() + 0.5 + size * Math.cos(i);
        double z = this.getBlockPos().getZ() + 0.5 + size * Math.sin(i);
        BlockPos pos = new BlockPos(x, this.getBlockPos().getY(), z);
        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, this.getBlockPos().getY() + 0.5, z, 10, 0.1, 0.1, 0.1, 0.1);
        serverLevel.sendParticles(ParticleTypes.SOUL, x, this.getBlockPos().getY() + 0.5, z, 10, 0.1, 0.1, 0.1, 0.1);
        EntityType<?> type = typeGetter.get();
        if (type != null) {
            boolean normalConditions = SpawnPlacements.checkSpawnRules(type, serverLevel, MobSpawnType.REINFORCEMENT, pos, this.level.random);
            boolean specialConditions = type.is(Spirit.SOUL_CAGE_CONDITIONS_IGNORED) || tier.ignoreSpawnConditions();
            if (normalConditions || specialConditions) {
                type.spawn(serverLevel, null, entity -> ((SoulContainingCreature) entity).setState(false), pos, MobSpawnType.REINFORCEMENT, true, false);
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Crystal")) {
            crystalStorage = ItemStack.of(tag.getCompound("Crystal"));
        }
        work = tag.getInt("Work");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        if (crystalStorage != null && !crystalStorage.isEmpty()) {
            compoundTag.put("Crystal", crystalStorage.save(new CompoundTag()));
        }
    }

    public @Nullable Entity getOrCreateEntity() {
        if (level != null && crystalStorage != null && !crystalStorage.isEmpty() && crystalStorage.getTag() != null) {
            if (entityStorage == null || !entityStorage.first.equals(crystalStorage.getTag().toString())) {
                if (crystalStorage.getItem() instanceof SoulContainingObject.Item item) {
                    SoulContainer container = item.getContainer(crystalStorage);
                    if (container != null) {
                        if (container.slotCount() > 0) {
                            entityStorage = Pair.of(crystalStorage.getTag().toString(), new ItemEntity(level, 0, 0, 0, crystalStorage));
                        } else {
                            SoulStack stack = container.getSoulStack(0);
                            if(stack.getEntity() != null) {
                                Entity o = stack.getEntity().create(level);
                                if (o != null) {
                                    entityStorage = Pair.of(crystalStorage.getTag().toString(), o);
                                }
                            }
                        }
                    }
                }
            }
            if (entityStorage != null) {
                return entityStorage.second;
            }
        }
        return null;
    }


    public boolean canInsertCrystal(ItemStack stack) {
        return this.isEmpty() && stack.getItem() instanceof Tierable tierable && tierable.getTier(stack) != null;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return crystalStorage == null || crystalStorage.isEmpty();
    }

    @Override
    public ItemStack getItem(int i) {
        return crystalStorage;
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return crystalStorage.split(j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        ItemStack stack = crystalStorage.copy();
        crystalStorage = null;
        return stack;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if(crystalStorage == null || crystalStorage.isEmpty()) {
            crystalStorage = itemStack;
            setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        crystalStorage = null;
    }

    public void update() {
        setChanged();
        if (level != null) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);

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
}
