package earth.terrarium.spirit.common.abilities.tool;

import com.google.common.collect.Multimap;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

// The more gold you have in your inventory, the more damage you deal.
public class SeeingGoldAbility implements ToolAbility {
    @Override
    public void modifyAttributes(ItemStack stack, Multimap<Attribute, AttributeModifier> attributes) {
        ToolAbility.super.modifyAttributes(stack, attributes);
    }

    @Override
    public ColorPalette getColor() {
        return null;
    }
}
