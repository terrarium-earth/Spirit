package earth.terrarium.spirit.common.mixin.fabric;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import earth.terrarium.spirit.common.util.AbilityUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class FabricLivingEntityMixin {
    @Shadow protected abstract ItemStack getLastArmorItem(EquipmentSlot equipmentSlot);

    @Shadow public abstract boolean equipmentHasChanged(ItemStack itemStack, ItemStack itemStack2);

    @Inject(method = "collectEquipmentChanges", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;equipmentHasChanged(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", shift = At.Shift.AFTER))
    private void spirit$collectEquipmentChanges(CallbackInfoReturnable<@Nullable Map<EquipmentSlot, ItemStack>> cir, @Local LocalRef<EquipmentSlot> slot) {
        if ((Object) (this) instanceof Player player) {
            ItemStack stack = getLastArmorItem(slot.get());
            ItemStack stack2 = player.getItemBySlot(slot.get());
            if (equipmentHasChanged(stack, stack2)) {
                if (stack.getItem() instanceof SoulSteelArmor) {
                    AbilityUtils.onArmorUnequip(player, slot.get(), stack);
                }

                if (stack2.getItem() instanceof SoulSteelArmor) {
                    AbilityUtils.onArmorEquip(player, slot.get(), stack2);
                }
            }
        }
    }
}
