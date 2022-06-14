package me.codexadrian.spirit.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SoulArrowEntity extends Arrow implements ItemSupplier {
    protected static final EntityDataAccessor<Boolean> EXPLOSIVE = SynchedEntityData.defineId(SoulArrowEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> EXPLOSION_POWER = SynchedEntityData.defineId(SoulArrowEntity.class, EntityDataSerializers.FLOAT);

    public SoulArrowEntity(EntityType<? extends Arrow> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getItem() {
        return Items.ARROW.getDefaultInstance();
    }

    public void enableExplosion(float explosionPower) {
        getEntityData().set(EXPLOSIVE, true);
        getEntityData().set(EXPLOSION_POWER, explosionPower);
    }

    public boolean isExplosive() {
        return getEntityData().get(EXPLOSIVE);
    }

    public float getExplosionPower() {
        return getEntityData().get(EXPLOSION_POWER);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(EXPLOSIVE, false);
        this.getEntityData().define(EXPLOSION_POWER, 2.0F);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("IsExplosive", getEntityData().get(EXPLOSIVE));
        compoundTag.putFloat("ExplosionPower", getEntityData().get(EXPLOSION_POWER));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        getEntityData().set(EXPLOSIVE, compoundTag.getBoolean("IsExplosive"));
        getEntityData().set(EXPLOSION_POWER, compoundTag.getFloat("ExplosionPower"));
    }

    @Override
    public void tick() {
        super.tick();
        //TODO fix soul particles bein crazy
        var vec3 = this.getDeltaMovement();
        double e = vec3.x;
        double f = vec3.y;
        double g = vec3.z;
        double h = this.getX() + e;
        double j = this.getY() + f;
        double k = this.getZ() + g;
        for (int i = 0; i < 4; i++) {
            this.level.addParticle(ParticleTypes.SOUL, h - e * 0.25, j - f * 0.25, k - g * 0.25, e, f, g);
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if(this.isExplosive()) {
            Vec3 location = entityHitResult.getLocation();
            this.level.explode(entityHitResult.getEntity(), location.x(), location.y(), location.z(), this.getExplosionPower(), Explosion.BlockInteraction.NONE);
        }
    }

    @Override
    public void setEnchantmentEffectsFromEntity(@NotNull LivingEntity arg, float f) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, arg);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, arg);
        //Overridden to literally fix this one line. piss off mojang -_-.
        this.setBaseDamage((f * this.getBaseDamage()) + this.random.triangle((double)this.level.getDifficulty().getId() * 0.11, 0.57425));
        if (i > 0) {
            this.setBaseDamage(this.getBaseDamage() + (double)i * 0.5 + 0.5);
        }
        if (j > 0) {
            this.setKnockback(j);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, arg) > 0) {
            this.setSecondsOnFire(100);
        }
    }
}
