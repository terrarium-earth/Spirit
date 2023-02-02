package earth.terrarium.spirit.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class ScytheItem extends SwordItem {
    public ScytheItem(Properties properties) {
        super(ScytheMaterial.INSTANCE, 0, 0, properties);
    }
}
