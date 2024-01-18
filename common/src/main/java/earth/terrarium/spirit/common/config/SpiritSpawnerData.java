package earth.terrarium.spirit.common.config;

public record SpiritSpawnerData(int minSpawnDelay, int maxSpawnDelay, int spawnCount) {
    public static final SpiritSpawnerData NORMAL = new SpiritSpawnerData(200, 400, 4);
    public static final SpiritSpawnerData CRITICAL = new SpiritSpawnerData(150, 300, 7);
}
