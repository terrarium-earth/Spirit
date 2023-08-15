package earth.terrarium.spirit.common.abilities.tool;

import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

// Steals health from mobs.
public class LifestealAbility implements ToolAbility {
    @Override
    public boolean onHit(DamageSource source, LivingEntity victim, float amount) {
        if (source.getEntity() instanceof Player player) {
            player.heal(amount * 0.1f);
        }
        return true;
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFF34283A, 0xFF1F1B29, 0xFF4F3153);
    }
}
