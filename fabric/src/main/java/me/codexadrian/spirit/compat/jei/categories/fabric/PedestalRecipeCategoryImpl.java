package me.codexadrian.spirit.compat.jei.categories.fabric;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;

public class PedestalRecipeCategoryImpl {

    public static ItemStack getSpawnEggStack(EntityType<?> entityType) {
        SpawnEggItem spawnEggItem = SpawnEggItem.byId(entityType);
        if(spawnEggItem != null) {
            return spawnEggItem.getDefaultInstance();
        }
        return ItemStack.EMPTY;
    }
}
