package me.codexadrian.spirit.forge;

import net.minecraftforge.common.ForgeConfigSpec;

public class SpiritConfigImpl {

    public static final ForgeConfigSpec.BooleanValue COLLECT_FROM_SOULLESS;
    public static final ForgeConfigSpec.IntValue SOUL_PEDESTAL_RANGE;
    public static final ForgeConfigSpec.IntValue CRUDE_SOUL_CRYSTAL_CAP;
    public static final ForgeConfigSpec.ConfigValue<String> INITIAL_TIER_NAME;
    public static final ForgeConfigSpec.BooleanValue SHOW_CHIPPED_ERROR;
    public static final ForgeConfigSpec CONFIG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Spirit Options");
        COLLECT_FROM_SOULLESS = builder.comment("Set this to true if you want mobs that spawn from a soul cage to be collectable in a soul crystal.").define("collectFromSoulless", false);
        SOUL_PEDESTAL_RANGE = builder.comment("This is the range that the soul pedestal will attract mobs from to insert into it.").defineInRange("soulPedestalRange", 3, 1, 6);
        CRUDE_SOUL_CRYSTAL_CAP = builder.comment("This value determines the maximum amount of souls a crude soul crystal can hold.").defineInRange("crudeSoulCrystalCap", 256, 1, Integer.MAX_VALUE);
        INITIAL_TIER_NAME = builder.comment("This is what the name of the tier is when the soul crystal has yet to reach a tier. It is translatable.").define("initialTierName", "spirit.soul_cage.tier_0");
        SHOW_CHIPPED_ERROR = builder.comment("Set this to false if you wish to not see the erorr on chipped blocks").define("showChippedTooltipError", true);
        builder.pop();
        CONFIG = builder.build();
    }

    public static boolean isCollectFromCorrupt() {
        return COLLECT_FROM_SOULLESS.get();
    }

    public static int getSoulPedestalRadius() {
        return SOUL_PEDESTAL_RANGE.get();
    }

    public static int getCrudeSoulCrystalCap() {
        return CRUDE_SOUL_CRYSTAL_CAP.get();
    }

    public static String getInitialTierName() {
        return INITIAL_TIER_NAME.get();
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static boolean showChippedError() {
        return SHOW_CHIPPED_ERROR.get();
    }
}
