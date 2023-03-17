package earth.terrarium.spirit.common.blockentity;

import com.ibm.icu.impl.Pair;
import com.teamresourceful.resourcefullib.common.collections.WeightedCollection;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.souls.SoulData;
import earth.terrarium.spirit.api.souls.SoulfulCreature;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.storage.Tierable;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class SoulCageBlockEntity extends BlockEntity implements WorldlyContainer {
    private int work = 0;
    private double spin = 0;
    private NonNullList<ItemStack> crystalStorage = NonNullList.withSize(5, ItemStack.EMPTY);
    private Pair<String, Entity> entityStorage;
    private SoulData data;
    private AABB boundingBox;

    public SoulCageBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.SOUL_CAGE.get(), blockPos, blockState);
    }

    public void tick() {
        if (!(level instanceof ServerLevel serverLevel)) {
            if (this.isNearPlayer()) {
                double spinAmount = 20D;
                SoulData tier = getPerformance();
                if (tier != null && tier.isRedstoneControlled() && level.hasNeighborSignal(this.getBlockPos())) {
                    spinAmount /= 30;
                } else {
                    double d = (double) getBlockPos().getX() + level.random.nextDouble();
                    double e = (double) getBlockPos().getY() + level.random.nextDouble();
                    double f = (double) getBlockPos().getZ() + level.random.nextDouble();
                    level.addParticle(ParticleTypes.SCULK_SOUL, d, e, f, 0.0D, 0.0D, 0.0D);
                    level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d, e, f, 0.0D, 0.0D, 0.0D);
                }
                this.spin = (this.spin + spinAmount) % 360D;
            }
            return;
        }
        if (!this.isEmpty()) {
            SoulData tier = getPerformance();
            if (tier != null && isNearPlayer()) {
                if (work < tier.getWorkTime()) {
                    if(work % 2 == 0) {
                        double fraction = (double) work / tier.getWorkTime();
                        double radius = tier.getSpawnRange() * fraction;
                        double increment = 3f / (2 * Math.PI * radius);
                        for (double i = 0; i < 2 * Math.PI; i += increment) {
                            double x = this.getBlockPos().getX() + 0.5;
                            double z = this.getBlockPos().getZ() + 0.5;
                            serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, x + radius * Math.cos(i), this.getBlockPos().getY() + 0.5, z + radius * Math.sin(i), 1, 0, 0, 0, 0);
                            if (work % 10 == 0)
                                serverLevel.sendParticles(ParticleTypes.SCULK_SOUL, x + radius * Math.cos(i), this.getBlockPos().getY() + 0.5, z + radius * Math.sin(i), 1, 0, 0, 0, 0);
                        }
                    }
                    work++;
                } else {
                    work = 0;
                    if (crystalStorage.get(0).getItem() instanceof SoulContainingObject.Item item) {
                        SoulContainer container = item.getContainer(crystalStorage.get(0));
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
                                    for (double i = 0; i < 2 * Math.PI; i += Math.PI / tier.getSpawnCount()) {
                                        spawnMob(serverLevel, tier, i, collection::next);
                                    }
                                }
                            } else {
                                SoulStack stack = container.getSoulStack(0);
                                for (double i = 0; i < 2 * Math.PI; i += Math.PI / tier.getSpawnCount()) {
                                    spawnMob(serverLevel, tier, i, stack::getEntity);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void spawnMob(ServerLevel serverLevel, SoulData tier, double i, Supplier<EntityType<?>> typeGetter) {
        double size = tier.getSpawnRange();
        double x = this.getBlockPos().getX() + 0.5 + size * Math.cos(i);
        double z = this.getBlockPos().getZ() + 0.5 + size * Math.sin(i);
        BlockPos pos = new BlockPos(x, this.getBlockPos().getY(), z);
        EntityType<?> type = typeGetter.get();
        if (type != null) {
            boolean normalConditions = SpawnPlacements.checkSpawnRules(type, serverLevel, MobSpawnType.SPAWNER, pos, this.level.random);
            boolean specialConditions = type.is(Spirit.SOUL_CAGE_CONDITIONS_IGNORED) || tier.isIgnoreSpawnConditions();
            if (normalConditions || specialConditions) {
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x, this.getBlockPos().getY() + 0.5, z, 10, 0.1, 0.1, 0.1, 0.1);
                serverLevel.sendParticles(ParticleTypes.SOUL, x, this.getBlockPos().getY() + 0.5, z, 10, 0.1, 0.1, 0.1, 0.1);
                type.spawn(serverLevel, null, entity -> ((SoulfulCreature) entity).setIfSoulless(true), pos, MobSpawnType.SPAWNER, true, false);
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, crystalStorage);
        work = tag.getInt("Work");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, crystalStorage);
    }

    public @Nullable Entity getOrCreateEntity() {
        if (level != null && crystalStorage != null && !crystalStorage.isEmpty()) {
            if (entityStorage == null || !entityStorage.first.equals(crystalStorage.get(0).getTag().toString())) {
                if (crystalStorage.get(0).getItem() instanceof SoulContainingObject.Item item) {
                    SoulContainer container = item.getContainer(crystalStorage.get(0));
                    if (container != null) {
                        SoulStack stack = container.getSoulStack(0);
                        if(stack.getEntity() != null) {
                            Entity o = stack.getEntity().create(level);
                            if (o != null) {
                                entityStorage = Pair.of(crystalStorage.get(0).getTag().toString(), o);
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

    private boolean isNearPlayer() {
        BlockPos blockPos = this.getBlockPos();
        BlockState blockState = getLevel().getBlockState(getBlockPos());
        if (blockState.is(SpiritBlocks.SOUL_CAGE.get())) {
            int size = level.getEntitiesOfClass(LivingEntity.class, getAABB()).size();
            if (size > 20) {
                return false;
            }
            SoulData tier = getPerformance();
            if (tier == null) {
                return false;
            } else if (tier.getNearbyRange() > 0) {
                return this.getLevel().hasNearbyAlivePlayer((double) blockPos.getX() + 0.5D,
                        (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, tier.getNearbyRange());
            } else {
                return true;
            }
        }
        return false;
    }


    public boolean canInsertCrystal(ItemStack stack) {
        return this.isEmpty() && stack.getItem() instanceof Tierable tierable && tierable.getSoulData(stack) != null;
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
        return crystalStorage.isEmpty() || crystalStorage.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int i) {
        return crystalStorage.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int j) {
        return ContainerHelper.removeItem(crystalStorage, i, j);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        return ContainerHelper.takeItem(crystalStorage, i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        crystalStorage.set(i, itemStack);
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

    public void reset() {
        work = 0;
        update();
    }

    public double getSpin() {
        return spin;
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

    @Nullable
    public SoulData getPerformance() {
        if (level != null && crystalStorage != null && !crystalStorage.isEmpty()) {
            if (data == null || work % 10 == 0) {
                if (crystalStorage.get(0).getItem() instanceof Tierable item) {
                    SoulData container = item.getSoulData(crystalStorage.get(0));
                    if (container != null) {
                        data = container;
                        return data;
                    }
                }
            }
        } else {
            data = null;
        }
        return data;
    }

    public AABB getAABB() {
        return boundingBox == null ? boundingBox = new AABB(getBlockPos()).inflate(10) : boundingBox;
    }
}
