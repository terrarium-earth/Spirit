package me.codexadrian.spirit;

import me.codexadrian.spirit.platform.Services;

import java.io.IOException;

public class Spirit {

    private static SpiritConfig spiritConfig;

    public static SpiritConfig getSpiritConfig() {
        return spiritConfig;
    }

    public static void onInitialize() {
        try {
            spiritConfig = SpiritConfig.loadConfig(Services.PLATFORM.getConfigDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
