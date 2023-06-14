package earth.terrarium.spirit.common.abilities.tool;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.common.util.AbilityUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class MagmaratorAbililty extends ToolAbility {
    @Override
    public void modifyDrops(Player player, List<ItemStack> drops) {
        for (int i = 0; i < drops.size(); i++) {
            ItemStack itemStack = drops.get(i);
            if (itemStack.isEdible()) {
                drops.set(i, AbilityUtils.getCookedResult(player.level(), itemStack));
            }
        }
    }

    @Override
    public ColorPalette getColor() {
        return Spirit.SOUL_COLOR_PALETTE;
    }
}
