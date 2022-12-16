package earth.terrarium.spirit.api.souls;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.spirit.common.config.items.CrystalConfig;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record Tier(String displayName, int requiredSouls, int minSpawnDelay, int maxSpawnDelay,
                   int spawnCount, int spawnRange, int nearbyRange,
                   boolean redstoneControlled, boolean ignoreSpawnConditions) {

    public static final Codec<Tier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("displayName").forGetter(Tier::displayName),
            Codec.INT.fieldOf("requiredSouls").forGetter(Tier::requiredSouls),
            Codec.INT.fieldOf("minSpawnDelay").forGetter(Tier::minSpawnDelay),
            Codec.INT.fieldOf("maxSpawnDelay").forGetter(Tier::maxSpawnDelay),
            Codec.INT.fieldOf("spawnCount").forGetter(Tier::spawnCount),
            Codec.INT.fieldOf("spawnRange").forGetter(Tier::spawnRange),
            Codec.INT.fieldOf("nearbyRange").forGetter(Tier::nearbyRange),
            Codec.BOOL.fieldOf("redstoneControlled").orElse(false).forGetter(Tier::redstoneControlled),
            Codec.BOOL.fieldOf("ignoreSpawnConditions").orElse(false).forGetter(Tier::ignoreSpawnConditions)
    ).apply(instance, Tier::new));
}
