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

public class SpiritClientConfig {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    @SerializedName("isShaderEnabled")
    private boolean isShaderEnabled = true;

    public boolean getShaderStatus() {
        return isShaderEnabled;
    }

    public void setShaderStatus(boolean newStatus) {
        isShaderEnabled = newStatus;
    }

    public static SpiritClientConfig loadConfig(Path configFolder) throws IOException {
        Path configPath = configFolder.resolve(Spirit.MODID + "_client.json");

        if (!Files.exists(configPath)) {
            SpiritClientConfig config = new SpiritClientConfig();
            try (Writer writer = new FileWriter(configPath.toFile())) {
                GSON.toJson(config, writer);
            }
            LOGGER.info("Created config file for mod " + Spirit.MODID);

            return config;
        }

        return GSON.fromJson(new InputStreamReader(Files.newInputStream(configPath)), SpiritClientConfig.class);
    }
}
