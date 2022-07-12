package me.codexadrian.spirit.items;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class SoulMetalMaterial implements Tier {
    public static final SoulMetalMaterial INSTANCE = new SoulMetalMaterial();

    @Override
    public int getUses() {
        return 500;
    }

    @Override
    public float getSpeed() {
        return 10.5F;
    }

    @Override
    public float getAttackDamageBonus() {
        return 3.5F;
    }

    @Override
    public int getLevel() {
        return 3;
    }

    @Override
    public int getEnchantmentValue() {
        return 25;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }
}