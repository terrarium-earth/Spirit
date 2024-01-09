package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.common.util.KeybindUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlazingHeightsAbility implements ArmorAbility {
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (KeybindUtils.jumpKeyDown(player)) {
            player.fallDistance /= 2;
            double speed = 0.5;
            player.setDeltaMovement(player.getDeltaMovement().add(0.0, speed, 0.0));
            if (player.getDeltaMovement().y() > speed) {
                player.setDeltaMovement(player.getDeltaMovement().x(), speed, player.getDeltaMovement().z());
            }

            if (level instanceof ServerLevel serverLevel) {
                double tickCount = player.tickCount / 40F;
                serverLevel.sendParticles(ParticleTypes.FLAME, player.getX() + Math.sin(tickCount), player.getY() + 0.75, player.getZ() + Math.cos(tickCount), 5, 0, 0.25, 0, 0);
                serverLevel.sendParticles(ParticleTypes.FLAME, player.getX() + Math.sin(tickCount + Math.PI/2), player.getY() + 0.75, player.getZ() + Math.cos(tickCount + Math.PI/2), 5, 0, 0.25, 0, 0);
                serverLevel.sendParticles(ParticleTypes.FLAME, player.getX() + Math.sin(tickCount + Math.PI), player.getY() + 0.75, player.getZ() + Math.cos(tickCount + Math.PI), 5, 0, 0.25, 0, 0);
                serverLevel.sendParticles(ParticleTypes.FLAME, player.getX() + Math.sin(tickCount + 3 * Math.PI/2), player.getY() + 0.75, player.getZ() + Math.cos(tickCount + 3 * Math.PI/2), 5, 0, 0.25, 0, 0);
            }
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFFFC700, 0xFFE77000, 0xFFFFF04E);
    }
}
