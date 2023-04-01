package earth.terrarium.spirit.common.abilities;

import earth.terrarium.spirit.api.armor_abilities.ArmorAbility;
import earth.terrarium.spirit.api.armor_abilities.ColorPalette;
import earth.terrarium.spirit.api.armor_abilities.EffectAbility;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AxolotlAbility extends EffectAbility {
    public AxolotlAbility() {
        super(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 0, true, true, true));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (player.isUnderWater() || level.isRaining()) {
            super.onArmorTick(stack, level, player);
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xdb88c1);
    }
}
