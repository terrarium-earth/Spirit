package earth.terrarium.spirit.api.abilities.armor;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.ColorPalette;

public class BlankArmorAbility implements ArmorAbility {
    @Override
    public ColorPalette getColor() {
        return Spirit.SOUL_COLOR_PALETTE;
    }
}
