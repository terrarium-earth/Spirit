package earth.terrarium.spirit.common.handlers;

import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import earth.terrarium.spirit.common.item.tools.SoulSteelTool;
import net.minecraft.world.item.ItemStack;

public class SoulAbilityHandler {
    public static float soulArmorProperty(ItemStack itemStack) {
        if (itemStack.getItem() instanceof SoulSteelArmor item) {
            return item.getAbility(itemStack) == null ? 0 : 1;
        }
        return 0;
    }

    public static float toolAbilityProperty(ItemStack itemStack) {
        if (itemStack.getItem() instanceof SoulSteelTool item) {
            return item.getAbility(itemStack) == null ? 0 : 1;
        }
        return 0;
    }
}
