package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EndermanAbility implements ArmorAbility {
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (player.isInWater()) {
            player.hurt(player.damageSources().magic(), 2.0F);
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFF419C92, 0x29766D, 0xFFC3FAF2);
    }
}
