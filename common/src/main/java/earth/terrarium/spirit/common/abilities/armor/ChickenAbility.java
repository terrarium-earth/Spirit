package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChickenAbility extends ArmorAbility {
    @Override
    public void onEntityMove(ItemStack stack, LivingEntity livingEntity, Level level, BlockPos blockPos) {
        if(livingEntity.calculateFallDamage(livingEntity.fallDistance, 1.0f) > 0) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 0, true, true, true));
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFff7a16, 0xFFdb4a12, 0xFFffc22e);
    }
}
