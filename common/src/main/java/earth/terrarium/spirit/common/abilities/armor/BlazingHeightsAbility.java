package earth.terrarium.spirit.common.abilities.armor;

import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;

public class BlazingHeightsAbility implements ArmorAbility {
    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFFFC700, 0xFFE77000, 0xFFFFF04E);
    }
}
