package earth.terrarium.spirit.api.armor_abilities;

import earth.terrarium.spirit.Spirit;

public class BlankAbility implements ArmorAbility {
    @Override
    public ColorPalette getColor() {
        return Spirit.SOUL_COLOR_PALETTE;
    }
}
