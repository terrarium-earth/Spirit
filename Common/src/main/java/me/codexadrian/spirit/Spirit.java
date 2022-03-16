package me.codexadrian.spirit;

import me.codexadrian.spirit.platform.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Spirit {
    public static final String MODID = "spirit";
    public static final Logger LOGGER = LogManager.getLogger();
    private static SpiritConfig spiritConfig;

    public static SpiritConfig getSpiritConfig() {
        return spiritConfig;
    }
    
    public void onInitialize() {
        try {
            spiritConfig = SpiritConfig.loadConfig(Services.PLATFORM.getConfigDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
