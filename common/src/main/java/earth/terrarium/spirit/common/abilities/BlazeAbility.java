package earth.terrarium.spirit.common.abilities;

import earth.terrarium.spirit.api.armor_abilities.ArmorAbility;
import earth.terrarium.spirit.api.armor_abilities.ColorPalette;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlazeAbility implements ArmorAbility {
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if(player.isOnFire()) {
            player.extinguishFire();
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0, true, true, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, true, true, true));
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFff7a16, 0xFFdb4a12, 0xFFffc22e);
    }
}
