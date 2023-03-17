package earth.terrarium.spirit.common.config.items;

import earth.terrarium.spirit.api.souls.SoulData;

public class CrystalConfig {

    public static String initialTierName = "spirit.soul_cage.tier_0";

    public static int minRequiredAmount = 64;
    public static int soulCrystalCap = 512;
    public static int crudeCrystalCap = 768;

    public static SoulData defaultData = new SoulData(384, 5, 7, 15, 1.0, false, false);
}
