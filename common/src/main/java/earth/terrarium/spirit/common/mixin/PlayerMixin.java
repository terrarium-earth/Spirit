package earth.terrarium.spirit.common.mixin;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Shadow public abstract Iterable<ItemStack> getArmorSlots();

    @Inject(at = @At("HEAD"), method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;")
    private void spirit$onEat(Level level, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
        for (ItemStack armorSlot : this.getArmorSlots()) {
            if (armorSlot.getItem() instanceof SoulSteelArmor armor) {
                ArmorAbility ability = armor.getAbility(armorSlot);
                if (ability != null) {
                    ability.onEat(armorSlot, itemStack, level, (Player) (Object) this);
                }
            }
        }
    }
}
