package earth.terrarium.spirit.common.item.tools;

import earth.terrarium.spirit.common.item.tools.ScytheMaterial;
import earth.terrarium.spirit.common.item.tools.SoulSteelTool;
import net.minecraft.world.item.SwordItem;

public class ScytheItem extends SwordItem implements SoulSteelTool {
    public ScytheItem(Properties properties) {
        super(ScytheMaterial.INSTANCE, 3, -2.4f, properties);
    }
}
