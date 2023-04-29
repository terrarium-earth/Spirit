package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

public class CowAbility extends ArmorAbility {
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (level.isClientSide) return;
        if (player.tickCount % 20 == 0) {
            ArrayList<MobEffectInstance> instancesCopy = new ArrayList<>(player.getActiveEffects());
            instancesCopy.forEach(effectInstance -> {
                if (effectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    player.removeEffect(effectInstance.getEffect());
                }
            });
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFff7a16, 0xFFdb4a12, 0xFFffc22e);
    }
}
