package earth.terrarium.spirit.api.rituals.components;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public interface RitualComponentSerializer<T extends RitualComponent<T>> {
    ResourceLocation id();
    Codec<T> codec();
}
