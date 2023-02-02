package earth.terrarium.spirit.api.elements;

import net.minecraft.network.chat.Component;

public enum BaseElements implements SoulElement {
    EMBER(0xff7a16, "elements.spirit.ember"),
    WATER(0x28b3ae, "elements.spirit.water"),
    EARTH(0x84c83d, "elements.spirit.earth"),
    ENDER(0xda58eb, "elements.spirit.ender");
    final String name;
    final int color;

    BaseElements(int color, String name) {
        this.color = color;
        this.name = name;
    }


    @Override
    public int getColor() {
        return color;
    }

    @Override
    public Component getName() {
        return Component.translatable(name);
    }
}
