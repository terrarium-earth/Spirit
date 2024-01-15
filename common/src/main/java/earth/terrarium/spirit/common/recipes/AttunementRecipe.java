package earth.terrarium.spirit.common.recipes;

import net.minecraft.world.entity.EntityType;

public interface AttunementRecipe {
    EntityType<?> entityType();
    boolean canBeCritical();
}
