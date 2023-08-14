package earth.terrarium.spirit.common.mixin.fabric;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.common.registry.SpiritAbilities;
import earth.terrarium.spirit.common.util.AbilityUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownEnderpearl.class)
public abstract class EnderpearlProjectileMixin extends Projectile {

    public EnderpearlProjectileMixin(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", shift = At.Shift.BEFORE), cancellable = true)
    private void spirit$onHit(HitResult hitResult, CallbackInfo ci) {
        if (this.getOwner() instanceof Player player && AbilityUtils.hasArmorAbility(player, SpiritAbilities.ENDERMAN)) {
            this.discard();
            ci.cancel();
        }
    }
}
