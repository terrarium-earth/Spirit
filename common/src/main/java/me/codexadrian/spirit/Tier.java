package me.codexadrian.spirit;

import com.google.gson.annotations.SerializedName;

public class Tier {

    @SerializedName("requiredSouls")
    private int requiredSouls;

    @SerializedName("minSpawnDelay")
    private int minSpawnDelay;

    @SerializedName("maxSpawnDelay")
    private int maxSpawnDelay;

    @SerializedName("spawnCount")
    private int spawnCount;

    @SerializedName("spawnRange")
    private int spawnRange;

    @SerializedName("nearbyRange")
    private int nearbyRange;

    @SerializedName("redstoneControlled")
    private boolean redstoneControlled;

    @SerializedName("ignoreSpawnConditions")
    private boolean ignoreSpawnConditions;

    @SerializedName("blacklist")
    private String[] blacklist;

    public Tier(int requiredSouls, int minSpawnDelay, int maxSpawnDelay, int spawnCount, int spawnRange, int nearbyRange,
                boolean redstoneControlled, boolean ignoreSpawnConditions, String[] blacklist) {
        this.requiredSouls = requiredSouls;
        this.minSpawnDelay = minSpawnDelay;
        this.maxSpawnDelay = maxSpawnDelay;
        this.spawnCount = spawnCount;
        this.spawnRange = spawnRange;
        this.nearbyRange = nearbyRange;
        this.redstoneControlled = redstoneControlled;
        this.ignoreSpawnConditions = ignoreSpawnConditions;
        this.blacklist = blacklist;
    }

    public int getRequiredSouls() {
        return requiredSouls;
    }

    public int getMinSpawnDelay() {
        return minSpawnDelay;
    }

    public int getMaxSpawnDelay() {
        return maxSpawnDelay;
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public int getSpawnRange() {
        return spawnRange;
    }

    public int getNearbyRange() {
        return nearbyRange;
    }

    public boolean isRedstoneControlled() {
        return redstoneControlled;
    }

    public boolean shouldIgnoreSpawnConditions() {
        return ignoreSpawnConditions;
    }

    public String[] getBlacklist() {
        return blacklist;
    }
}
