package earth.terrarium.spirit.api.rituals.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import earth.terrarium.spirit.api.rituals.components.impl.BasinComponent;
import earth.terrarium.spirit.api.rituals.components.impl.ItemComponent;
import earth.terrarium.spirit.api.rituals.components.impl.SoulComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RitualComponentManager {
    public static final Codec<RitualComponentSerializer<?>> COMPONENT_CODEC = ResourceLocation.CODEC.comapFlatMap(RitualComponentManager::decode, RitualComponentSerializer::id);
    public static final Codec<RitualComponent<?>> CODEC = COMPONENT_CODEC.dispatch(RitualComponent::getSerializer, RitualComponentSerializer::codec);

    private static final Map<ResourceLocation, RitualComponentSerializer<?>> SERIALIZERS = new HashMap<>();

    static {
        add(BasinComponent.SERIALIZER);
        add(ItemComponent.SERIALIZER);
        add(SoulComponent.SERIALIZER);
    }

    private static DataResult<? extends RitualComponentSerializer<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(SERIALIZERS.get(id)).map(DataResult::success).orElse(DataResult.error(() -> "No ritual component type found."));
    }

    public static void add(RitualComponentSerializer<?> serializer) {
        SERIALIZERS.put(serializer.id(), serializer);
    }
}