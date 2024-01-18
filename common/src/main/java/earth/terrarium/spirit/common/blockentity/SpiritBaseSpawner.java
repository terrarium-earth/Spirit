package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.api.utils.SoulfulCreature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public abstract class SpiritBaseSpawner {
    private static final int SPAWN_RADIUS = 4;

	private int spawnDelay = 20;
	private boolean requiresPlayer = true;
	private double spin;
	private double oSpin;

	@Nullable
	private Entity displayEntity;

	private boolean isNearPlayer(Level level, BlockPos pos) {
		if (!this.requiresPlayer) return true;
        int requiredPlayerRange = 16;
        return level.hasNearbyAlivePlayer((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, requiredPlayerRange);
	}

	public void clientTick(Level level, BlockPos pos) {
		if (!this.isNearPlayer(level, pos)) {
			this.oSpin = this.spin;
		} else if (this.displayEntity != null) {
			RandomSource randomSource = level.getRandom();
			double d = (double)pos.getX() + randomSource.nextDouble();
			double e = (double)pos.getY() + randomSource.nextDouble();
			double f = (double)pos.getZ() + randomSource.nextDouble();
			level.addParticle(ParticleTypes.SOUL, d, e, f, 0.0, 0.0, 0.0);
			level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d, e, f, 0.0, 0.0, 0.0);
			if (this.spawnDelay > 0) {
				--this.spawnDelay;
			}

			this.oSpin = this.spin;
			this.spin = (this.spin + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0;
		}

	}

	public void serverTick(ServerLevel serverLevel, BlockPos pos) {
		if (getEntityToSpawn() == null) return;
		if (this.isNearPlayer(serverLevel, pos)) {
			if (this.spawnDelay == -1) {
				this.delay(serverLevel, pos);
			}

			if (this.spawnDelay > 0) {
				--this.spawnDelay;
			} else {
				boolean bl = false;
				RandomSource randomSource = serverLevel.getRandom();

				for(int i = 0; i < this.getSpawnCount(); ++i) {
					double x = (double)pos.getX() + (randomSource.nextDouble() - 0.5) * SPAWN_RADIUS * 2;
					double y = pos.getY() + randomSource.nextInt(3) - 1;
					double z = (double)pos.getZ() + (randomSource.nextDouble() - 0.5) * SPAWN_RADIUS * 2;

					if (serverLevel.noCollision(getEntityToSpawn().getAABB(x, y, z))) {
						BlockPos blockPos = BlockPos.containing(x, y, z);
						if (!SpawnPlacements.checkSpawnRules(getEntityToSpawn(), serverLevel, MobSpawnType.SPAWNER, blockPos, serverLevel.getRandom())) {
							continue;
						}

						Entity entity = getEntityToSpawn().spawn(serverLevel, blockPos, MobSpawnType.SPAWNER);

						if (entity == null) {
							this.delay(serverLevel, pos);
							return;
						}

						int k = serverLevel.getEntitiesOfClass(entity.getClass(), new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).inflate(SPAWN_RADIUS)).size();
						if (k >= this.getMaxNearbyEntities()) {
							this.delay(serverLevel, pos);
							return;
						}

						entity.moveTo(entity.getX(), entity.getY(), entity.getZ(), randomSource.nextFloat() * 360.0F, 0.0F);
						if (entity instanceof Mob mob) {
							if (!mob.checkSpawnRules(serverLevel, MobSpawnType.SPAWNER) || !mob.checkSpawnObstruction(serverLevel)) {
								continue;
							}

							mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.SPAWNER, null, null);
						}

						if (entity instanceof SoulfulCreature soulless) {
							soulless.setIfSoulless(true);
						}

						if (!serverLevel.tryAddFreshEntityWithPassengers(entity)) {
							this.delay(serverLevel, pos);
							return;
						}

						serverLevel.levelEvent(2004, pos, 0);
						serverLevel.gameEvent(entity, GameEvent.ENTITY_PLACE, blockPos);
						if (entity instanceof Mob mob) {
							mob.spawnAnim();
						}

						bl = true;
					}
				}

				if (bl) {
					this.delay(serverLevel, pos);
				}
			}
		}
	}

	private void delay(Level level, BlockPos pos) {
		RandomSource randomSource = level.random;
		if (this.getMaxSpawnDelay() <= this.getMinSpawnDelay()) {
			this.spawnDelay = this.getMinSpawnDelay();
		} else {
			this.spawnDelay = this.getMinSpawnDelay() + randomSource.nextInt(this.getMaxSpawnDelay() - this.getMinSpawnDelay());
		}
		this.broadcastEvent(level, pos, 1);
	}

	@Nullable
	public Entity getOrCreateDisplayEntity(Level level) {
		if (this.displayEntity == null && this.getEntityToSpawn() != null) {
			this.displayEntity = getEntityToSpawn().create(level);
		}

		return this.displayEntity;
	}

	public boolean onEventTriggered(Level level, int id) {
		if (id == 1) {
			if (level.isClientSide) {
				this.spawnDelay = this.getMinSpawnDelay();
			}

			return true;
		} else {
			return false;
		}
	}

	public abstract void broadcastEvent(Level level, BlockPos pos, int eventId);

	public double getSpin() {
		return this.spin;
	}

	public double getoSpin() {
		return this.oSpin;
	}

	public abstract int getMinSpawnDelay();

	public abstract int getMaxSpawnDelay();

	public abstract int getSpawnCount();

	public abstract EntityType<?> getEntityToSpawn();

	public int getMaxNearbyEntities() {
		return (int) (getSpawnCount() * 1.5);
	}

	public void setPlayerRequirement(boolean requiresPlayer) {
		this.requiresPlayer = requiresPlayer;
	}
}
