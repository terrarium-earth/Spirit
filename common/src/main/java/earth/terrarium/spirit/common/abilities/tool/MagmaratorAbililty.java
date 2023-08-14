package earth.terrarium.spirit.common.abilities.tool;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.common.util.AbilityUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

// All drops from blocks are cooked
public class MagmaratorAbililty implements ToolAbility {
    @Override
    public void modifyDrops(Player player, List<ItemStack> drops) {
        drops.replaceAll(stack -> AbilityUtils.getCookedResult(player.level(), stack));
    }

    @Override
    public ColorPalette getColor() {
        return Spirit.SOUL_COLOR_PALETTE;
    }
}
