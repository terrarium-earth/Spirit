package earth.terrarium.spirit.common.config.items;

import earth.terrarium.spirit.api.souls.Tier;

import java.util.List;

public class CrystalConfig {

    public static String initialTierName = "spirit.soul_cage.tier_0";

    public static int soulCrystalCap = 512;
    public static int crudeCrystalCap = 768;

    public static List<Tier> tiers = List.of(
            new Tier("spirit.soul_cage.tier_1", 64, 512, 1024, 3, 5, 7, false, false),
            new Tier("spirit.soul_cage.tier_2", 128, 256, 512, 5, 5, 12, false, false),
            new Tier("spirit.soul_cage.tier_3", 256, 128, 256, 7, 5, 17, false, false),
            new Tier("spirit.soul_cage.tier_4", 512, 64, 128, 9, 5, -1, false, false)
    );
}
