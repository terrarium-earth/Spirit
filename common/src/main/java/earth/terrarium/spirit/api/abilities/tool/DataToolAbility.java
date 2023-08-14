package earth.terrarium.spirit.api.abilities.tool;

import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.nbt.CompoundTag;

public class DataToolAbility implements ToolAbility {

    public void deserialize(CompoundTag tag) {
    }

    public CompoundTag serialize(CompoundTag tag) {
        return tag;
    }

    @Override
    public ColorPalette getColor() {
        return null;
    }
}
