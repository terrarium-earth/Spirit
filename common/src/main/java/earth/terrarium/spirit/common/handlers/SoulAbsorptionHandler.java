package earth.terrarium.spirit.common.handlers;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.souls.SoulfulCreature;
import earth.terrarium.spirit.api.storage.AutoAbsorbing;
import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.api.utils.SoulUtils;
import earth.terrarium.spirit.common.config.SpiritConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulAbsorptionHandler {

    public static void onEntityDeath(LivingEntity victim, DamageSource source) {
        SoulfulCreature creature = (SoulfulCreature) victim;
        if (victim.level instanceof ServerLevel level) {
            Entity entity = source.getEntity();
            if (entity instanceof Projectile projectile) entity = projectile.getOwner();
            if (entity instanceof Player player) {
                if (!victim.getType().is(Spirit.COLLECT_BLACKLISTED_TAG)) {
                    if (victim.canChangeDimensions() && (SpiritConfig.collectFromSoulless || !creature.isSoulless())) {
                        /*
                        boolean pedestalHasCrystal = false;
                        int radius = SoulConcentratorConfig.range;
                        AABB entityArea = victim.getBoundingBox().inflate(radius, 2, radius);
                        Optional<BlockPos> pedestalPos = BlockPos.betweenClosedStream(entityArea).filter(pos -> level.getBlockState(pos).is(SpiritBlocks.CRYSTAL_PEDESTAL.get())).map(BlockPos::immutable).findFirst();
                        if (pedestalPos.isPresent() && level.getBlockEntity(pedestalPos.get()) instanceof PedestalBlockEntity pedestal && !pedestal.isEmpty() && SoulUtils.canCrystalAcceptSoul(pedestal.getItem(0), victim)) {
                            crystal = pedestal.getItem(0);
                            pedestalHasCrystal = true;
                            pedestal.setChanged();
                            level.sendBlockUpdated(pedestalPos.get(), pedestal.getBlockState(), pedestal.getBlockState(), Block.UPDATE_ALL);
                        }
                         */
                        ItemStack crystal = getSoulCrystal(player, victim);
                        if (!crystal.isEmpty()) {
                            SoulContainer container = SoulUtils.getContainer(crystal);
                            if(container != null) {
                                container.insert(new SoulStack(victim.getType(), 1), InteractionMode.NO_TAKE_BACKSIES);
                                level.sendParticles(ParticleTypes.SOUL, victim.getX() + 0.5, victim.getY() + victim.getBbHeight() / 2, victim.getZ() + 0.5, 10, 0.5, 0.5, 0.5, 0.1);
                                /*
                                if (pedestalPos.isPresent() && pedestalHasCrystal) {
                                    ServerLevel sLevel = (ServerLevel) player.level;
                                    sLevel.sendParticles(ParticleTypes.SOUL, pedestalPos.get().getX() + 0.5, pedestalPos.get().getY() + 0.5, pedestalPos.get().getZ() + 0.5, 15, 0.5, 1, 0.5, 0);
                                    sLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, pedestalPos.get().getX() + 0.5, pedestalPos.get().getY() + 0.5, pedestalPos.get().getZ() + 0.5, 15, 0.5, 1, 0.5, 0);
                                }
                                 */
                            }
                        }
                    }
                }
            }
        }
    }

    public static ItemStack getSoulCrystal(Player player, @Nullable LivingEntity victim) {
        ItemStack savedStack = ItemStack.EMPTY;
        int savedSouls = 0;
        for (var currentInventory : List.of(player.getHandSlots(), player.getInventory().items, player.getArmorSlots())) {
            for(var currentItem : currentInventory) {
                if (!(currentItem.getItem() instanceof SoulContainingObject.Item crystal && currentItem.getItem() instanceof AutoAbsorbing autoAbsorbing)) {
                    continue;
                }
                SoulContainer container = crystal.getContainer(currentItem);
                if (container == null) {
                    continue;
                }
                if (container.insert(new SoulStack(victim == null ? null : victim.getType(), 1), InteractionMode.SIMULATE) == 1) {
                    if (savedStack.isEmpty()) {
                        savedStack = currentItem;
                    } else {
                        if (currentItem.getTag() != null) {
                            //if the current savedStack either is empty or has some amount of souls in it. therefore any new crystal that's empty
                            //is either equal to or worse than the saved stack, so the current item, if it is empty, should be skipped.
                            int souls = container.getSoulStack(0).getAmount();
                            if (souls > savedSouls || autoAbsorbing.getPriority() > ((AutoAbsorbing) savedStack.getItem()).getPriority()) {
                                savedStack = currentItem;
                                savedSouls = souls;
                            }
                        }
                    }
                }
            }
        }
        return savedStack;
    }
}
