package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.common.config.SpiritSpawnerData;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SoulCageBlockEntity extends BlockEntity {
    private SpiritSpawnerData spawnerData;
    private boolean critical = false;
    private EntityType<?> entityType;

    private final SpiritBaseSpawner spawner = new SpiritBaseSpawner() {
        @Override
        public void broadcastEvent(Level level, BlockPos pos, int eventId) {
            level.blockEvent(pos, SpiritBlocks.SOUL_CAGE.get(), eventId, 0);
        }

        @Override
        public int getMinSpawnDelay() {
            return spawnerData == null ? 200 : spawnerData.minSpawnDelay();
        }

        @Override
        public int getMaxSpawnDelay() {
            return spawnerData == null ? 400 : spawnerData.maxSpawnDelay();
        }

        @Override
        public int getSpawnCount() {
            return spawnerData == null ? 4 : spawnerData.spawnCount();
        }

        @Override
        public EntityType<?> getEntityToSpawn() {
            return entityType;
        }
    };

    public SoulCageBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.SOUL_CAGE.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        entityType = EntityType.byString(tag.getString("Entity")).orElse(null);
        critical = tag.getBoolean("Critical");
        spawnerData = critical ? SpiritSpawnerData.CRITICAL : SpiritSpawnerData.NORMAL;
        spawner.setPlayerRequirement(!critical);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Entity", EntityType.getKey(entityType).toString());
        tag.putBoolean("Critical", critical);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SoulCageBlockEntity blockEntity) {
        blockEntity.spawner.clientTick(level, pos);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SoulCageBlockEntity blockEntity) {
        blockEntity.spawner.serverTick((ServerLevel)level, pos);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public void setEntityId(EntityType<?> type) {
        this.entityType = type;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
        this.spawnerData = critical ? SpiritSpawnerData.CRITICAL : SpiritSpawnerData.NORMAL;
        this.spawner.setPlayerRequirement(!critical);
    }

    public EntityType<?> getEntityId() {
        return this.entityType;
    }

    public SpiritBaseSpawner getSpawner() {
        return this.spawner;
    }
}
