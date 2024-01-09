package earth.terrarium.spirit.common.item.tools;

import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public class ScytheMaterial implements Tier {
    public static final ScytheMaterial INSTANCE = new ScytheMaterial();

    @Override
    public int getUses() {
        return 768;
    }

    @Override
    public float getSpeed() {
        return 12;
    }

    @Override
    public float getAttackDamageBonus() {
        return 5;
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public int getEnchantmentValue() {
        return 20;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(SpiritItems.CRYSTAL_SHARD.get());
    }
}
