package earth.terrarium.spirit.common.abilities.tool;

import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;

// Tools do not get their mining speed slowed by any outside effects.
public class HighlyAdaptiveAbility implements ToolAbility {
    @Override
    public ColorPalette getColor() {
        return null;
    }
}
