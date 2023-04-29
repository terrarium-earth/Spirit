package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlazeAbility extends ArmorAbility {
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if(player.isOnFire()) {
            player.extinguishFire();
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, 0, true, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0, true, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0, true, true, true));
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFff7a16, 0xFFdb4a12, 0xFFffc22e);
    }
}
