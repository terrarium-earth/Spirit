package earth.terrarium.spirit.common.entity;

import earth.terrarium.spirit.api.souls.SoulfulCreature;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SoulReceptacle extends Entity implements SoulfulCreature {
    ItemStack result;
    int processTime;
    int maxProcessTime;

    public SoulReceptacle(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        result = ItemStack.of(compoundTag.getCompound("Result"));
        processTime = compoundTag.getInt("ProcessTime");
        maxProcessTime = compoundTag.getInt("MaxProcessTime");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.put("Result", result.save(new CompoundTag()));
        compoundTag.putInt("ProcessTime", processTime);
        compoundTag.putInt("MaxProcessTime", maxProcessTime);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    public void setResult(ItemStack result, int maxProcessTime) {
        this.result = result;
        this.maxProcessTime = maxProcessTime;
        this.processTime = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getLevel() instanceof ServerLevel serverLevel) {
            if (result != null) {
                if (processTime < maxProcessTime) {
                    //summon particles in a circle around this position
                    for (double i = 0; i < 2 * Math.PI * ((double) this.processTime / this.maxProcessTime); i += 0.2) {
                        serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getX() + Math.cos(i), this.getY() + 0.5, this.getZ() + Math.sin(i), 1, 0, 0, 0, 0);
                    }
                    processTime++;
                } else {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY()  + 0.5, this.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                    serverLevel.sendParticles(ParticleTypes.SOUL, this.getX(), this.getY()  + 0.5, this.getZ(), 20, 0.5, 0.5, 0.5, 0.1);
                    serverLevel.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), result));
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }
    }

    @Override
    public boolean isSoulless() {
        return true;
    }

    @Override
    public void setIfSoulless(boolean state) {
        //do nothing
    }
}
