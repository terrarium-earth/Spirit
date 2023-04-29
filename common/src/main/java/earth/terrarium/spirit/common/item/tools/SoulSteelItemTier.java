package earth.terrarium.spirit.common.item.tools;

import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class SoulSteelItemTier implements Tier {
    public static final SoulSteelItemTier INSTANCE = new SoulSteelItemTier();

    @Override
    public int getUses() {
        return 1500;
    }

    @Override
    public float getSpeed() {
        return 2;
    }

    @Override
    public float getAttackDamageBonus() {
        return 5;
    }

    @Override
    public int getLevel() {
        return 15;
    }

    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return Ingredient.of(SpiritItems.SOUL_STEEL_INGOT.get());
    }
}
