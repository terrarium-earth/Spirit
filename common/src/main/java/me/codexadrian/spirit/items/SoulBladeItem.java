package me.codexadrian.spirit.items;

import me.codexadrian.spirit.Spirit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class SoulBladeItem extends SwordItem {
    public SoulBladeItem(Properties properties) {
        super(new SoulSwordMaterial(), 3, -2.4F, properties);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return Spirit.SOUL_COLOR;
    }

    public static class SoulSwordMaterial implements Tier {
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
}
