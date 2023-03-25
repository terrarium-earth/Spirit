package earth.terrarium.spirit.common.mixin;

import earth.terrarium.spirit.common.item.trinkets.BaseTrinket;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinAi.class)
public class PiglinMixin {

    @Inject(method = "isWearingGold", at = @At("HEAD"), cancellable = true)
    @Debug
    private static void spirit$isWearingGold(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (livingEntity instanceof Player player && player.getInventory().hasAnyMatching(itemStack -> itemStack.getItem() == SpiritItems.PIGLIN_CHARM.get() && BaseTrinket.isEnabled(itemStack))) {
            cir.setReturnValue(true);
        }
    }
}
