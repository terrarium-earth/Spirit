package earth.terrarium.spirit.common.handlers;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.api.utils.SoulfulCreature;
import earth.terrarium.spirit.common.config.SpiritConfig;
import earth.terrarium.spirit.common.item.crystals.SoulCrystalItem;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biomes;
import org.apache.commons.lang3.NotImplementedException;

public class SoulAbsorptionHandler {

    public static void onEntityDeath(LivingEntity victim, DamageSource source) {
        if (victim.level() instanceof ServerLevel serverLevel) {
            double odds = SpiritConfig.baseDropChance;

            if (serverLevel.getBiome(victim.blockPosition()).is(Biomes.SOUL_SAND_VALLEY)) {
                odds += SpiritConfig.ambientDropChance;
            }

            if (source.getEntity() instanceof Player player) {
                odds += getPetrificationBonus(victim, player);
            }

            if (odds > 0) {
                ItemStack rewardStack;

                if (Math.random() * 100 < odds) {
                    rewardStack = SoulCrystalItem.createSoulCrystal(victim.getType());
                } else {
                    rewardStack = new ItemStack(SpiritItems.CRYSTAL_SHARD.get(), (int) (Math.random() * 6));
                }

                victim.spawnAtLocation(rewardStack);
            }
        }
    }

    @ExpectPlatform
    public static int getPetrificationBonus(LivingEntity victim, Player player) {
        throw new NotImplementedException("Petrification is not implemented yet");
    }
}
