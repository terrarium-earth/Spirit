package earth.terrarium.spirit.common.abilities.tool;

import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

// Right-clicking with a tool will place a glowing particle effect on the floor
public class GlowSquidAbility implements ToolAbility {
    @Override
    public void whileHeld(Player player, ItemStack stack) {
        player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 100));
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFff7a16, 0xFFdb4a12, 0xFFffc22e);
    }
}
