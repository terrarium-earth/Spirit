package me.codexadrian.spirit;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class SoulSwordMaterial implements Tier {
    @Override
    public int getUses() {
        return 200;
    }

    @Override
    public float getSpeed() {
        return 9;
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
