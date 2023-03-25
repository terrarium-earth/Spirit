package earth.terrarium.spirit.common.handlers;

import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import net.minecraft.world.item.ItemStack;

public class SoulArmorHandler {
    public static float soulArmorProperty(ItemStack itemStack) {
        if (itemStack.getItem() instanceof SoulSteelArmor item) {
            return item.getAbility(itemStack) == null ? 0 : 1;
        }
        return 0;
    }
}
