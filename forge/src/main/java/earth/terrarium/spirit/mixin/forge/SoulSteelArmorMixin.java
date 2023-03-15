package earth.terrarium.spirit.mixin.forge;

import earth.terrarium.spirit.client.forge.SpiritArmorExtension;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(SoulSteelArmor.class)
public abstract class SoulSteelArmorMixin {
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new SpiritArmorExtension());
    }
}
