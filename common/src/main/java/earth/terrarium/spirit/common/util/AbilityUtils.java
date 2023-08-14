package earth.terrarium.spirit.common.util;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AbilityUtils {
    //get the cooked result of an item
    public static ItemStack getCookedResult(Level level, ItemStack itemStack) {
        ItemStack result = itemStack.copy();
        Optional<SmeltingRecipe> first = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING).stream().filter(recipe -> recipe.getIngredients().get(0).test(result)).findFirst();
        return first.map(smeltingRecipe -> smeltingRecipe.getResultItem(level.registryAccess()).copy()).orElse(itemStack);
    }

    public static void onArmorEquip(Player player, EquipmentSlot slot, ItemStack stack) {
        if (stack.getItem() instanceof SoulSteelArmor armor) {
            ArmorAbility ability = armor.getAbility(stack);
            if (ability != null) {
                ability.onEquip(player, slot, stack);
            }
        }
    }

    public static void onArmorUnequip(Player player, EquipmentSlot slot, ItemStack stack) {
        if (stack.getItem() instanceof SoulSteelArmor armor) {
            ArmorAbility ability = armor.getAbility(stack);
            if (ability != null) {
                ability.onUnequip(player, slot, stack);
            }
        }
    }

    public static boolean hasArmorAbility(Player player, ArmorAbility ability, @Nullable EquipmentSlot slot) {
        if (slot == null) {
            for (ItemStack stack : player.getArmorSlots()) {
                if (stack.getItem() instanceof SoulSteelArmor armor) {
                    if (armor.getAbility(stack) == ability) {
                        return true;
                    }
                }
            }
        } else {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof SoulSteelArmor armor) {
                return armor.getAbility(stack) == ability;
            }
        }
        return false;
    }

    public static boolean hasArmorAbility(Player player, ArmorAbility ability) {
        return hasArmorAbility(player, ability, null);
    }
}
