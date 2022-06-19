package me.codexadrian.spirit.mixin;

import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.traits.KnockbackTrait;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "getKnockbackBonus", at = @At("RETURN"))
    private static void getTraitKnockbackBonus(LivingEntity livingEntity, CallbackInfoReturnable<Integer> cir) {
        if(livingEntity instanceof Player player) {
            ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true, true);
            var entityEffect = MobTraitData.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(Objects.requireNonNull(SoulUtils.getSoulCrystalType(soulCrystal)))), livingEntity.getLevel().getRecipeManager());
            if(entityEffect.isPresent()) {
                int knockback = 0;
                for(var trait : entityEffect.get().traits()) {
                    if(trait instanceof KnockbackTrait knockbackTrait) {
                        knockback += knockbackTrait.knockback();
                    }
                }
                cir.setReturnValue(cir.getReturnValueI() + knockback);
            }
        }
    }

    @Inject(method = "getDamageBonus", at = @At("RETURN"))
    private static void getTraitDamageBonus(ItemStack itemStack, MobType mobType, CallbackInfoReturnable<Float> cir) {

    }
}
