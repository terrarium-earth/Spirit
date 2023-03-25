package earth.terrarium.spirit.common.abilities;

import earth.terrarium.spirit.api.armor_abilities.ArmorAbility;
import earth.terrarium.spirit.api.armor_abilities.ColorPalette;

public class WolfAbility implements ArmorAbility {
    public ColorPalette getColor() {
        return new ColorPalette(0xFFc4b6a7, 0xFFa69a8b, 0xFFd8c9ba);
    }
}
