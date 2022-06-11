package me.codexadrian.spirit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

public class SpiritConfig {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @SerializedName("maxAmountCrudeSoulCrystalCanStore")
    private int crudeSoulCrystalCap = 256;

    @SerializedName("soulPedestalRadius")
    private int soulPedestalRadius = 3;

    @SerializedName("tiers")
    private Tier[] tiers = new Tier[]{
            new Tier(64, 300, 1000, 3, 5, 16, false, false, new String[0]),
            new Tier(128, 150, 600, 5, 7, 24, false, false, new String[0]),
            new Tier(256, 75, 400, 7, 9, 32, false, false, new String[0]),
            new Tier(512, 25, 300, 9, 11, -1, true, false, new String[0])
    };

    @SerializedName("collectFromCorrupt")
    private boolean collectFromCorrupt = false;

    @SerializedName("entityBlacklist")
    private String[] blacklist = new String[0];

    public Tier[] getTiers() {
        return tiers;
    }

    public boolean isCollectFromCorrupt() {
        return collectFromCorrupt;
    }

    public String[] getBlacklist() {
        return blacklist;
    }

    public int getCrudeSoulCrystalCap() {
        return crudeSoulCrystalCap;
    }

    public int getSoulPedestalRadius() {
        return soulPedestalRadius;
    }

    public static SpiritConfig loadConfig(Path configFolder) throws IOException {
        Path configPath = configFolder.resolve(Constants.MODID + ".json");

        if (!Files.exists(configPath))
            return generateDefault(configPath);

        try {
            SpiritConfig config = GSON.fromJson(new InputStreamReader(Files.newInputStream(configPath)), SpiritConfig.class);
            Arrays.sort(config.getTiers(), Comparator.comparing(Tier::getRequiredSouls));
            if (config.getBlacklist() == null)
                config.blacklist = new String[0];

            return config;
        } catch (Exception e) {
            LOGGER.error("Error parsing config file for mod " + Constants.MODID);
        }

        return generateDefault(configPath);
    }

    private static SpiritConfig generateDefault(Path configPath) throws IOException {
        SpiritConfig config = new SpiritConfig();
        Arrays.sort(config.getTiers(), Comparator.comparing(Tier::getRequiredSouls));
        try (Writer writer = new FileWriter(configPath.toFile())) {
            GSON.toJson(config, writer);
        }

        LOGGER.info("Created config file for mod " + Constants.MODID);
        return config;
    }
}
