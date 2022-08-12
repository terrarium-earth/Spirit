package me.codexadrian.spirit.compat.jei.categories.forge;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class PedestalRecipeCategoryImpl {

    public static ItemStack getSpawnEggStack(EntityType<?> entityType) {
        SpawnEggItem spawnEggItem = ForgeSpawnEggItem.fromEntityType(entityType);
        if(spawnEggItem != null) {
            return spawnEggItem.getDefaultInstance();
        }
        return ItemStack.EMPTY;
    }
}
