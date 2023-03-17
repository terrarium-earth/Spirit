package earth.terrarium.spirit.api.souls;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SoulData {
    private int workTime;
    private int spawnCount;
    private int spawnRange;
    private int nearbyRange;
    private double efficiency;
    private boolean redstoneControlled;
    private boolean ignoreSpawnConditions;

    public static final Codec<SoulData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("workTime").forGetter(SoulData::getWorkTime),
            Codec.INT.fieldOf("spawnCount").forGetter(SoulData::getSpawnCount),
            Codec.INT.fieldOf("spawnRange").forGetter(SoulData::getSpawnRange),
            Codec.INT.fieldOf("nearbyRange").forGetter(SoulData::getNearbyRange),
            Codec.DOUBLE.fieldOf("efficiency").forGetter(SoulData::getEfficiency),
            Codec.BOOL.fieldOf("redstoneControlled").orElse(false).forGetter(SoulData::isRedstoneControlled),
            Codec.BOOL.fieldOf("ignoreSpawnConditions").orElse(false).forGetter(SoulData::isIgnoreSpawnConditions)
    ).apply(instance, SoulData::new));

    public SoulData(int workTime, int spawnCount, int spawnRange, int nearbyRange, double efficiency, boolean redstoneControlled, boolean ignoreSpawnConditions) {
        this.workTime = workTime;
        this.spawnCount = spawnCount;
        this.spawnRange = spawnRange;
        this.nearbyRange = nearbyRange;
        this.efficiency = efficiency;
        this.redstoneControlled = redstoneControlled;
        this.ignoreSpawnConditions = ignoreSpawnConditions;
    }

    public int getWorkTime() {
        return workTime;
    }

    public SoulData setWorkTime(int workTime) {
        this.workTime = workTime;
        return this;
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public SoulData setSpawnCount(int spawnCount) {
        this.spawnCount = spawnCount;
        return this;
    }

    public int getSpawnRange() {
        return spawnRange;
    }

    public SoulData setSpawnRange(int spawnRange) {
        this.spawnRange = spawnRange;
        return this;
    }

    public int getNearbyRange() {
        return nearbyRange;
    }

    public SoulData setNearbyRange(int nearbyRange) {
        this.nearbyRange = nearbyRange;
        return this;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public SoulData setEfficiency(double efficiency) {
        this.efficiency = efficiency;
        return this;
    }

    public boolean isRedstoneControlled() {
        return redstoneControlled;
    }

    public SoulData setRedstoneControlled(boolean redstoneControlled) {
        this.redstoneControlled = redstoneControlled;
        return this;
    }

    public boolean isIgnoreSpawnConditions() {
        return ignoreSpawnConditions;
    }

    public SoulData setIgnoreSpawnConditions(boolean ignoreSpawnConditions) {
        this.ignoreSpawnConditions = ignoreSpawnConditions;
        return this;
    }
}
