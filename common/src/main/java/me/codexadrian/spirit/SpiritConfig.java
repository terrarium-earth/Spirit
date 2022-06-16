package me.codexadrian.spirit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import dev.architectury.injectables.annotations.ExpectPlatform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

public class SpiritConfig {

    @Contract(pure = true)
    @ExpectPlatform
    public static boolean isCollectFromCorrupt() {
        throw new AssertionError();
    }

    @Contract(pure = true)
    @ExpectPlatform
    public static String getInitialTierName() {
        throw new AssertionError();
    }

    @Contract(pure = true)
    @ExpectPlatform
    public static int getCrudeSoulCrystalCap() {
        throw new AssertionError();
    }
    @Contract(pure = true)
    @ExpectPlatform
    public static int getSoulPedestalRadius() {
        throw new AssertionError();
    }

    @Contract(pure = true)
    @ExpectPlatform
    public static boolean showChippedError() {
        throw new AssertionError();
    }
}
