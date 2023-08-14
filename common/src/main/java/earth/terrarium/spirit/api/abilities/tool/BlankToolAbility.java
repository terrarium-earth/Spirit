package earth.terrarium.spirit.api.abilities.tool;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.ColorPalette;

public class BlankToolAbility implements ToolAbility {
    @Override
    public ColorPalette getColor() {
        return Spirit.SOUL_COLOR_PALETTE;
    }
}
