package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BeeAbility extends ArmorAbility {
    @Override
    public void onEat(ItemStack armor, ItemStack food, Level level, Player player) {
        FoodProperties props = food.getItem().getFoodProperties();
        if (food.is(Spirit.SWEETS) && props != null) {
            player.heal(props.getNutrition());
        }
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFc4b6a7, 0xFFa69a8b, 0xFFd8c9ba);
    }
}
