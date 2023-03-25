package earth.terrarium.spirit.common.item.armor;

import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

// TODO set actual values
public class SoulSteelArmorMaterial implements ArmorMaterial {

    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
    private static final int[] PROTECTION_VALUES = new int[]{2, 5, 6, 2};

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return BASE_DURABILITY[type.getSlot().getIndex()] * 25;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return PROTECTION_VALUES[type.getSlot().getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return 14;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_DIAMOND;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(SpiritItems.SOUL_STEEL_INGOT.get()); // TODO should use tag
    }

    @Override
    public String getName() {
        return "soul_steel";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}