package me.codexadrian.spirit.entity;

import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.ToolType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulArrowEntity extends Arrow implements ItemSupplier {
    @Nullable
    public MobTraitData effect;

    public SoulArrowEntity(EntityType<? extends Arrow> entityType, Level level) {
        super(entityType, level);
    }

    public void addArrowEffect(MobTraitData effect) {
        this.effect = effect;
        effect.traits().forEach(soulArrowTrait -> soulArrowTrait.initializeArrow(this));
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
        if (this.level.isClientSide) {
            if (this.inGround) {
                if (this.inGroundTime % 5 == 0) {
                    this.makeSoulParticle(1);
                }
            } else {
                this.makeSoulParticle(2);
            }
        }
    }

    public void makeSoulParticle(int i) {
        for (int k = 0; k < i; ++k) {
            this.level.addParticle(ParticleTypes.SOUL, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0, 0, 0);
            this.level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0, 0, 0);
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if(effect != null) {
            effect.traits().forEach(soulArrowTrait -> soulArrowTrait.onHitEntity(ToolType.BOW, this.getOwner(), entityHitResult.getEntity()));
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if(effect != null) {
            effect.traits().forEach(soulArrowTrait -> soulArrowTrait.onHitBlock(ToolType.BOW,this, level.getBlockState(blockHitResult.getBlockPos()), level, blockHitResult.getBlockPos()));
            this.discard();
        }
    }

    @Override
    public void setEnchantmentEffectsFromEntity(@NotNull LivingEntity arg, float f) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, arg);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, arg);
        //Overridden to literally fix this one line. piss off mojang -_-.
        this.setBaseDamage((f * this.getBaseDamage()) + this.random.nextGaussian() * 0.25D + (double)((float)this.level.getDifficulty().getId() * 0.11F));
        if (i > 0) {
            this.setBaseDamage(this.getBaseDamage() + (double) i * 0.5 + 0.5);
        }
        if (j > 0) {
            this.setKnockback(j);
        }
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, arg) > 0) {
            this.setSecondsOnFire(100);
        }
    }
}
