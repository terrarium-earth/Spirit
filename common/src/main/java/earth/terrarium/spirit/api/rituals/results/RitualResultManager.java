package earth.terrarium.spirit.api.rituals.results;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import earth.terrarium.spirit.api.rituals.results.impl.EntityResult;
import earth.terrarium.spirit.api.rituals.results.impl.ItemResult;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RitualResultManager {
    public static final Codec<RitualResultSerializer<?>> RESULT_CODEC = ResourceLocation.CODEC.comapFlatMap(RitualResultManager::decode, RitualResultSerializer::id);
    public static final Codec<RitualResult<?>> CODEC = RESULT_CODEC.dispatch(RitualResult::getSerializer, RitualResultSerializer::codec);

    private static final Map<ResourceLocation, RitualResultSerializer<?>> SERIALIZERS = new HashMap<>();

    static {
        add(EntityResult.SERIALIZER);
        add(ItemResult.SERIALIZER);
    }

    private static DataResult<? extends RitualResultSerializer<?>> decode(ResourceLocation id) {
        return Optional.ofNullable(SERIALIZERS.get(id)).map(DataResult::success).orElse(DataResult.error(() -> "No ritual result type found."));
    }

    public static void add(RitualResultSerializer<?> serializer) {
        SERIALIZERS.put(serializer.id(), serializer);
    }
}