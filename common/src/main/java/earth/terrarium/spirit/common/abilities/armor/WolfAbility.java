package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WolfAbility extends ArmorAbility {
    @Override
    public void onEntityMove(ItemStack stack, LivingEntity livingEntity, Level level, BlockPos blockPos) {
        if(livingEntity.isSprinting() && level.getRandom().nextFloat() < 0.1) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, true, true, true));
        }
    }

    public ColorPalette getColor() {
        return new ColorPalette(0xFFc4b6a7, 0xFFa69a8b, 0xFFd8c9ba);
    }
}
