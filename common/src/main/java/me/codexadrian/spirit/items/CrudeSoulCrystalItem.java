package me.codexadrian.spirit.items;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CrudeSoulCrystalItem extends Item {
    public CrudeSoulCrystalItem(Properties $$0) {
        super($$0);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return 0x00fffb;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        return (int) ((double) SoulUtils.getSoulsInCrystal(itemStack) / Spirit.getSpiritConfig().getCrudeSoulCrystalCap()) * 13;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        return itemStack.hasTag() && itemStack.getTag().contains("Souls");
    }
}
