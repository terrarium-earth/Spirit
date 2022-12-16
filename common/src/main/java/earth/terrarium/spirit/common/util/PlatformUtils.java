package earth.terrarium.spirit.common.util;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformUtils {

    @ExpectPlatform
    public static boolean isModLoaded(String modid) {
        return false;
    }
}
