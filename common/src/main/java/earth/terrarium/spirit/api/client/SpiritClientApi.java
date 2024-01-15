package earth.terrarium.spirit.api.client;

import earth.terrarium.spirit.api.rituals.results.RitualResult;

import java.util.HashMap;
import java.util.Map;

public class SpiritClientApi {
    public static final Map<Class<? extends RitualResult<?>>, ReceptacleRenderer> RECEPTACLE_RENDERERS = new HashMap<>();

    public static <T extends RitualResult<?>> void registerReceptacleRenderer(Class<T> clazz, ReceptacleRenderer renderer) {
        RECEPTACLE_RENDERERS.put(clazz, renderer);
    }

    public static <T extends RitualResult<?>> ReceptacleRenderer getReceptacleRenderer(Class<T> clazz) {
        return RECEPTACLE_RENDERERS.get(clazz);
    }
}
