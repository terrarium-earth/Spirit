package earth.terrarium.spirit.common.blockentity;

import com.teamresourceful.resourcefullib.common.collections.WeightedCollection;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.souls.SoulContainingCreature;
import earth.terrarium.spirit.api.souls.Tier;
import earth.terrarium.spirit.api.storage.SoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.storage.Tierable;
import earth.terrarium.spirit.api.utils.SoulStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.NaturalSpawner;

import java.util.function.Supplier;

public class SoulCageSpawner {
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
                            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x + size * Math.cos(i), this.getBlockPos().getY() + 0.5, z + size * Math.sin(i), 1, 0.1, 0.1, 0.1, 0.1);
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
            boolean normalConditions = NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, serverLevel, this.getBlockPos(), type) && SpawnPlacements.checkSpawnRules(type, serverLevel, MobSpawnType.REINFORCEMENT, pos, this.level.random);
            boolean specialConditions = type.is(Spirit.SOUL_CAGE_CONDITIONS_IGNORED) || tier.ignoreSpawnConditions();
            if (normalConditions || specialConditions) {
                type.spawn(serverLevel, null, entity -> ((SoulContainingCreature) entity).setState(false), pos, MobSpawnType.REINFORCEMENT, true, false);
            }
        }
    }
}
