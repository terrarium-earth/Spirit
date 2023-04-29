package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.armor.EffectAbility;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BatAbility extends EffectAbility {
    public BatAbility() {
        super(new MobEffectInstance(MobEffects.NIGHT_VISION, 400, 100));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if(!level.canSeeSky(player.blockPosition()) && level.getLightEmission(player.blockPosition()) < 8) {
            super.onArmorTick(stack, level, player);
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFff7a16, 0xFFdb4a12, 0xFFffc22e);
    }
}
