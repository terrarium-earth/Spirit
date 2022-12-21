package earth.terrarium.spirit.api.souls;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record Tier(String displayName, int requiredSouls, int workTime,
                   int spawnCount, int spawnRange, int nearbyRange,
                   boolean redstoneControlled, boolean ignoreSpawnConditions) {

    public static final Codec<Tier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("displayName").forGetter(Tier::displayName),
            Codec.INT.fieldOf("requiredSouls").forGetter(Tier::requiredSouls),
            Codec.INT.fieldOf("workTime").forGetter(Tier::workTime),
            Codec.INT.fieldOf("spawnCount").forGetter(Tier::spawnCount),
            Codec.INT.fieldOf("spawnRange").forGetter(Tier::spawnRange),
            Codec.INT.fieldOf("nearbyRange").forGetter(Tier::nearbyRange),
            Codec.BOOL.fieldOf("redstoneControlled").orElse(false).forGetter(Tier::redstoneControlled),
            Codec.BOOL.fieldOf("ignoreSpawnConditions").orElse(false).forGetter(Tier::ignoreSpawnConditions)
    ).apply(instance, Tier::new));
}
