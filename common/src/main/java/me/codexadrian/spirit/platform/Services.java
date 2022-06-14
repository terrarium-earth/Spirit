package me.codexadrian.spirit.platform;

import me.codexadrian.spirit.Constants;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.platform.services.IPlatformHelper;
import me.codexadrian.spirit.platform.services.IRegistryHelper;

import java.util.ServiceLoader;

public class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final IRegistryHelper REGISTRY = load(IRegistryHelper.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Spirit.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);

        return loadedService;
    }
}
