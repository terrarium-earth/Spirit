package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BounceAbility implements ArmorAbility {
    @Override
    public boolean onLand(Player player, EquipmentSlot slot, ItemStack stack, float distance) {
        if (!player.isShiftKeyDown()) {
            Vec3 vec3d = player.getDeltaMovement();
            player.setDeltaMovement(vec3d.x / 0.9, -vec3d.y * 0.99, vec3d.z / 0.9);
            player.hasImpulse = true;
            player.setOnGround(false);
            player.playSound(SoundEvents.SLIME_SQUISH, 1f, 1f);
            return true;
        }
        return false;
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFF00FF0A, 0xFF009D64, 0xFFB9FF9C);
    }
}
