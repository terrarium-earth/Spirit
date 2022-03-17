package me.codexadrian.spirit.blocks.soulcage;

import me.codexadrian.spirit.Corrupted;
import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.Tier;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SoulCageSpawner {

    private double spin;
    private int spawnDelay = 20;
    private final SoulCageBlockEntity soulCageBlockEntity;

    public SoulCageSpawner(SoulCageBlockEntity entity) {
        this.soulCageBlockEntity = entity;
    }

    public void tick() {
        Level level = this.getLevel();
        BlockPos blockPos = this.getPos();
        if (level.isClientSide) {
            if (this.isNearPlayer()) {
                double spinAmount = 20D;
                Tier tier = SoulUtils.getTier(soulCageBlockEntity.getItem(0));
                if (tier != null && tier.isRedstoneControlled() && level.hasNeighborSignal(soulCageBlockEntity.getBlockPos())) {
                    spinAmount /= 30;
                } else {
                    double d = (double) blockPos.getX() + level.random.nextDouble();
                    double e = (double) blockPos.getY() + level.random.nextDouble();
                    double f = (double) blockPos.getZ() + level.random.nextDouble();
                    level.addParticle(ParticleTypes.SOUL, d, e, f, 0.0D, 0.0D, 0.0D);
                    level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d, e, f, 0.0D, 0.0D, 0.0D);
                }
                this.spin = (this.spin + spinAmount) % 360D;
            }
        } else if (this.isNearPlayer()) {
            Tier tier = SoulUtils.getTier(soulCageBlockEntity.getItem(0));
            if (tier == null) {
                return;
            }

            if (this.spawnDelay == -1) {
                this.delay(tier);
            }

            if (this.spawnDelay > 0) {
                --this.spawnDelay;
                return;
            }

            if (tier.isRedstoneControlled() && level.hasNeighborSignal(soulCageBlockEntity.getBlockPos())) {
                return;
            }

            boolean bl = false;
            int i = 0;

            while (true) {
                if (i >= tier.getSpawnCount()) {
                    if (bl) {
                        this.delay(tier);
                    }
                    break;
                }

                if (soulCageBlockEntity.type == null) {
                    this.delay(tier);
                    return;
                }

                double x = blockPos.getX() + (level.random.nextDouble() - level.random.nextDouble()) * tier.getSpawnRange() + 0.5D;
                double y = blockPos.getY() + level.random.nextInt(3) - 1;
                double z = blockPos.getZ() + (level.random.nextDouble() - level.random.nextDouble()) * tier.getSpawnRange() + 0.5D;
                if (level.noCollision(soulCageBlockEntity.type.getAABB(x, y, z))) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    if (tier.shouldIgnoreSpawnConditions() || SpawnPlacements.checkSpawnRules(soulCageBlockEntity.type, serverLevel, MobSpawnType.SPAWNER, new BlockPos(x, y, z), level.getRandom())) {
                        Entity spawned = soulCageBlockEntity.type.create(level);
                        if (spawned == null) {
                            this.delay(tier);
                            return;
                        }
                        ((Corrupted) spawned).setCorrupted();
                        spawned.moveTo(x, y, z, spawned.getYRot(), spawned.getXRot());

                        int l = level.getEntitiesOfClass(spawned.getClass(), new AABB(blockPos).inflate(tier.getSpawnRange())).size();
                        if (l >= 6) {
                            this.delay(tier);
                            return;
                        }

                        spawned.moveTo(spawned.getX(), spawned.getY(), spawned.getZ(), level.random.nextFloat() * 360.0F, 0.0F);
                        if (spawned instanceof Mob) {
                            Mob mob = (Mob) spawned;
                            if ((!tier.shouldIgnoreSpawnConditions() && !mob.checkSpawnRules(level, MobSpawnType.SPAWNER)) || !mob.checkSpawnObstruction(level)) {
                                this.delay(tier);
                                return;
                            }

                            ((Mob) spawned).finalizeSpawn(serverLevel, level.getCurrentDifficultyAt(spawned.blockPosition()), MobSpawnType.SPAWNER, null, null);
                        }

                        if (!serverLevel.tryAddFreshEntityWithPassengers(spawned)) {
                            this.delay(tier);
                            return;
                        }

                        serverLevel.sendParticles(ParticleTypes.SOUL, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 20, 1, 1, 1, 0);

                        if (spawned instanceof Mob) {
                            ((Mob) spawned).spawnAnim();
                        }

                        bl = true;
                    }
                }

                ++i;
            }
        }
    }

    private boolean isNearPlayer() {
        BlockPos blockPos = this.getPos();
        BlockState blockState = getLevel().getBlockState(getPos());
        if (blockState.is(SpiritRegistry.SOUL_CAGE.get())) {
            Tier tier = SoulUtils.getTier(soulCageBlockEntity.getItem(0));
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

    private void delay(Tier tier) {
        if (tier.getMaxSpawnDelay() <= tier.getMinSpawnDelay()) {
            this.spawnDelay = tier.getMinSpawnDelay();
        } else {
            this.spawnDelay = tier.getMinSpawnDelay() +
                    this.getLevel().random.nextInt(tier.getMaxSpawnDelay() - tier.getMinSpawnDelay());
        }

        this.broadcastEvent(1);
    }

    public boolean onEventTriggered(int i) {
        if (i == 1 && this.getLevel().isClientSide) {
            Tier tier = SoulUtils.getTier(soulCageBlockEntity.getItem(0));
            if (tier == null) {
                return false;
            }

            this.spawnDelay = tier.getMinSpawnDelay();
            return true;
        } else {
            return false;
        }
    }

    public void broadcastEvent(int i) {
        Level level = soulCageBlockEntity.getLevel();
        if (level != null) {
            level.blockEvent(soulCageBlockEntity.getBlockPos(), SpiritRegistry.SOUL_CAGE.get(), i, 0);
        }
    }

    public Level getLevel() {
        return soulCageBlockEntity.getLevel();
    }

    public BlockPos getPos() {
        return soulCageBlockEntity.getBlockPos();
    }

    public double getSpin() {
        return spin;
    }

}
