package me.codexadrian.spirit.entity;

import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SoulArrowEntity extends Arrow implements ItemSupplier {


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
