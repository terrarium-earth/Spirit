package me.codexadrian.spirit.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class SoulArrowEntity extends AbstractArrow implements ItemSupplier {

    public SoulArrowEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
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
}
