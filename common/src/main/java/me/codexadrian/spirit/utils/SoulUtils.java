package me.codexadrian.spirit.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.SpiritConfig;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.registry.SpiritMisc;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SoulUtils {

    @Nullable
    public static Tier getTier(ItemStack itemStack, Level level) {
        if (itemStack.getTag() == null || !itemStack.getTag().contains("StoredEntity")) {
            return null;
        }

        return Tier.getTier(getSoulsInCrystal(itemStack), getSoulCrystalType(itemStack), level);
    }

    public static String getTierDisplay(ItemStack itemStack, Level level) {
        Tier tier = getTier(itemStack, level);
        return tier == null ? SpiritConfig.getInitialTierName() : tier.displayName();
    }

    @Contract(pure = true)
    @Nullable
    public static Tier getNextTier(ItemStack itemStack, Level level) {
        if (itemStack.getTag() == null || !itemStack.getTag().contains("StoredEntity")) {
            return null;
        }

        return Tier.getTier(getSoulsInCrystal(itemStack), getSoulCrystalType(itemStack), level, true);
    }

    public static int getMaxSouls(ItemStack itemStack, Level level) {
        if (itemStack.getTag() == null || !itemStack.getTag().contains("StoredEntity")) {
            return Integer.MAX_VALUE;
        }
        Tier maxTier = Tier.getHighestTier(getSoulCrystalType(itemStack), level);
        return maxTier == null ? Integer.MAX_VALUE : maxTier.requiredSouls();
    }

    public static int getSoulsInCrystal(ItemStack itemStack) {
        if (itemStack.getTag() != null) {
            if (itemStack.is(SpiritItems.SOUL_CRYSTAL.get())) {
                return itemStack.getTag().getCompound("StoredEntity").getInt("Souls");
            } else if (itemStack.is(SpiritItems.CRUDE_SOUL_CRYSTAL.get())) {
                return itemStack.getTag().getInt("Souls");
            } else if (itemStack.is(SpiritItems.SOUL_CRYSTAL_SHARD.get())) {
                return itemStack.getTag().contains("EntityType") ? 1 : 0;
            }
        }
        return 0;
    }

    public static boolean canCrystalAcceptSoul(ItemStack crystal, @Nullable LivingEntity victim) {
        if(victim == null) return canCrystalAcceptSoul(crystal, null, null);
        return canCrystalAcceptSoul(crystal, victim.getLevel(), victim.getType());
    }

    public static boolean canCrystalAcceptSoul(ItemStack crystal, @Nullable Level level, @Nullable EntityType<?> type) {
        if (crystal.is(SpiritItems.SOUL_CRYSTAL.get())) {
            if(type == null) return false;
            if (crystal.getTag() != null) {
                boolean isCorrectType = Registry.ENTITY_TYPE.getKey(type).toString().equals(getSoulCrystalType(crystal));
                boolean hasRoomForMore = getSoulsInCrystal(crystal) < getMaxSouls(crystal, level);
                return isCorrectType && hasRoomForMore;
            }
            if (type.equals(SpiritMisc.SOUL_ENTITY.get())) return false;
            //todo check to make sure it isnt of type plain soul
            return true;
        } else if (crystal.is(SpiritItems.CRUDE_SOUL_CRYSTAL.get())) {
            return SoulUtils.getSoulsInCrystal(crystal) < SpiritConfig.getCrudeSoulCrystalCap();
        } else if (crystal.is(SpiritItems.SOUL_CRYSTAL_SHARD.get())) {
            return SoulUtils.getSoulCrystalType(crystal) == null;
        }
        return false;
    }

    public static boolean doCrystalTypesMatch(ItemStack crystal1, ItemStack crystal2) {
        return Objects.equals(getSoulCrystalType(crystal1), getSoulCrystalType(crystal2));
    }

    @Nullable
    public static String getSoulCrystalType(ItemStack crystal) {
        if(crystal.is(SpiritItems.SOUL_CRYSTAL.get())) {
            if (crystal.getTag() != null) {
                String string = crystal.getTag().getCompound("StoredEntity").getString("Type");
                return string.isBlank() ? null : string;
            }
            return null;
        }
        if(crystal.is(SpiritItems.SOUL_CRYSTAL_SHARD.get())) {
            if (crystal.getTag() != null) {
                String string = crystal.getTag().getString("EntityType");
                return string.isBlank() ? null : string;
            }
            return null;
        }
        return null;
    }

    public static ItemStack findCrystal(Player player, @Nullable LivingEntity victim, boolean mustContainSouls, boolean mustBeSoulCrystal, boolean canFindMobCrystal) {
        ItemStack returnStack;
        returnStack = searchTrinkets(player, victim);
        if (returnStack.isEmpty() && canFindMobCrystal) returnStack = getMobCrystal(player.getHandSlots());
        if (returnStack.isEmpty()) returnStack = getSoulCrystal(player.getHandSlots(), victim, mustContainSouls);
        if (returnStack.isEmpty() && !mustBeSoulCrystal) returnStack = getCrudeSoulCrystal(player.getHandSlots(), mustContainSouls);
        if (returnStack.isEmpty() && canFindMobCrystal) returnStack = getMobCrystal(player.getInventory().items);
        if (returnStack.isEmpty()) returnStack = getSoulCrystal(player.getInventory().items, victim, mustContainSouls);
        if (returnStack.isEmpty() && !mustBeSoulCrystal) returnStack = getCrudeSoulCrystal(player.getInventory().items, mustContainSouls);
        return returnStack;
    }

    public static ItemStack findCrystal(Player player, @Nullable LivingEntity victim, boolean mustContainSouls) {
        return findCrystal(player, victim, mustContainSouls, false, false);
    }

    @ExpectPlatform
    public static ItemStack searchTrinkets(Player player, @Nullable LivingEntity victim) {
        throw new AssertionError();
    }

    public static ItemStack getSoulCrystal(Iterable<ItemStack> inventory, @Nullable LivingEntity victim, boolean mustContainSouls) {
        ItemStack savedStack = ItemStack.EMPTY;
        int savedSouls = 0;
        for (ItemStack currentItem : inventory) {
            if (!currentItem.is(SpiritItems.SOUL_CRYSTAL.get()) || (getSoulsInCrystal(currentItem) == 0 && mustContainSouls)) {
                continue;
            }
            if (victim == null) {
                return currentItem;
            }
            if (canCrystalAcceptSoul(currentItem, victim)) {
                if (savedStack.isEmpty()) {
                    savedStack = currentItem;
                } else {
                    if (currentItem.getTag() != null) {
                        //if the current savedStack either is empty or has some amount of souls in it. therefore any new crystal that's empty
                        //is either equal to or worse than the saved stack, so the current item, if it is empty, should be skipped.
                        int souls = getSoulsInCrystal(currentItem);
                        if (souls > savedSouls) {
                            savedStack = currentItem;
                            savedSouls = souls;
                        }
                    }
                }
            }
        }
        return savedStack;
    }

    public static ItemStack getCrudeSoulCrystal(Iterable<ItemStack> inventory, boolean mustContainSouls) {
        ItemStack savedStack = ItemStack.EMPTY;
        int savedSouls = 0;

        for (ItemStack currentItem : inventory) {
            if (!currentItem.is(SpiritItems.CRUDE_SOUL_CRYSTAL.get()) || (getSoulsInCrystal(currentItem) == 0 && mustContainSouls)) {
                continue;
            }

            if (!SoulUtils.canCrystalAcceptSoul(currentItem, null)) continue;

            int soulsInCrystal = getSoulsInCrystal(currentItem);
            if (savedStack.isEmpty() || soulsInCrystal > savedSouls) {
                savedStack = currentItem;
                savedSouls = soulsInCrystal;
            }
        }

        return savedStack;
    }

    public static ItemStack getMobCrystal(Iterable<ItemStack> inventory) {
        ItemStack savedStack = ItemStack.EMPTY;
        for (ItemStack currentItem : inventory) {
            if (!currentItem.is(SpiritItems.SOUL_CRYSTAL_SHARD.get())) {
                continue;
            }

            if (savedStack.isEmpty() && getSoulCrystalType(currentItem) == null) {
                savedStack = currentItem;
            }
        }

        return savedStack;
    }

    public static void handleMobCrystal(ItemStack mobCrystal, Player player, LivingEntity victim) {
        if (player.level instanceof ServerLevel serverLevel) {
            deviateSoulCount(mobCrystal, 1, player.level, Registry.ENTITY_TYPE.getKey(victim.getType()).toString());
            serverLevel.sendParticles(ParticleTypes.SOUL, victim.getX(), victim.getY(), victim.getZ(), 20, victim.getBbWidth(), victim.getBbHeight(), victim.getBbWidth(), 0);
            serverLevel.sendParticles(ParticleTypes.SOUL, player.getX(), player.getY(), player.getZ(), 40, 1, 2, 1, 0);
        }
    }

    public static void handleSoulCrystal(ItemStack soulCrystal, Player player, LivingEntity victim) {
        //Gravy seal of okayness
        if (player.level instanceof ServerLevel serverLevel) {
            CompoundTag storedEntity;
            if (soulCrystal.getTag() == null || !soulCrystal.getTag().contains("StoredEntity")) {
                CompoundTag tag = new CompoundTag();
                tag.putString("Type", Registry.ENTITY_TYPE.getKey(victim.getType()).toString());
                soulCrystal.getOrCreateTag().put("StoredEntity", tag);
                storedEntity = tag;
            } else {
                storedEntity = soulCrystal.getTag().getCompound("StoredEntity");
            }

            serverLevel.sendParticles(ParticleTypes.SOUL, victim.getX(), victim.getY(), victim.getZ(), 20, victim.getBbWidth(), victim.getBbHeight(), victim.getBbWidth(), 0);
            Tier tier = SoulUtils.getNextTier(soulCrystal, serverLevel);

            int incrementAmount = getSoulHarvestAmount(player);

            if (tier != null && storedEntity.getInt("Souls") + incrementAmount >= tier.requiredSouls()) {
                player.displayClientMessage(Component.translatable("item.spirit.soul_crystal.upgrade_message").withStyle(ChatFormatting.AQUA), true);
                serverLevel.sendParticles(ParticleTypes.SOUL, player.getX(), player.getY(), player.getZ(), 40, 1, 2, 1, 0);
            }

            storedEntity.putInt("Souls", storedEntity.getInt("Souls") + incrementAmount);
        }
    }

    public static void deviateSoulCount(ItemStack stack, int deviation, Level level, @Nullable String mobType) {
        if (stack.is(SpiritItems.SOUL_CRYSTAL_SHARD.get())) {
            if(deviation < 0) stack.getOrCreateTag().remove("EntityType");
            else if(mobType != null && deviation > 0) stack.getOrCreateTag().putString("EntityType", mobType);
            return;
        }
        if (stack.getTag() != null) {
            if (stack.is(SpiritItems.SOUL_CRYSTAL.get())) {
                CompoundTag storedEntity = stack.getTag().getCompound("StoredEntity");
                storedEntity.putInt("Souls", Mth.clamp(getSoulsInCrystal(stack) + deviation, 0, SoulUtils.getMaxSouls(stack, level)));
                if (storedEntity.getInt("Souls") == 0) {
                    stack.setTag(null);
                }
            } else if (stack.is(SpiritItems.CRUDE_SOUL_CRYSTAL.get())) {
                stack.getTag().putInt("Souls", Mth.clamp(getSoulsInCrystal(stack) + deviation, 0, SpiritConfig.getCrudeSoulCrystalCap()));
                if (stack.getTag().getInt("Souls") == 0) {
                    stack.setTag(null);
                }
            }
        } else {
            if (stack.is(SpiritItems.SOUL_CRYSTAL.get()) && mobType != null && deviation > 0) {
                CompoundTag storedEntity = new CompoundTag();
                storedEntity.putString("Type", mobType);
                storedEntity.putInt("Souls", deviation);
                stack.getOrCreateTag().put("StoredEntity", storedEntity);
            } else if (stack.is(SpiritItems.CRUDE_SOUL_CRYSTAL.get()) && deviation > 0) {
                stack.getOrCreateTag().putInt("Souls", Math.min(deviation, SpiritConfig.getCrudeSoulCrystalCap()));
            } else if (stack.is(SpiritItems.SOUL_CRYSTAL_SHARD.get()) && deviation > 0 && mobType != null) {
                stack.getOrCreateTag().putString("EntityType", mobType);
            }
        }
    }

    public static void handleCrudeSoulCrystal(ItemStack crudeCrystal, Player player, LivingEntity victim) {
        if (player.level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SOUL, victim.getX(), victim.getY(), victim.getZ(), 20, victim.getBbWidth(), victim.getBbHeight(), victim.getBbWidth(), 0);
            crudeCrystal.getOrCreateTag().putInt("Souls", Math.min(getSoulsInCrystal(crudeCrystal) + getSoulHarvestAmount(player), SpiritConfig.getCrudeSoulCrystalCap()));
        }
    }

    public static int getSoulHarvestAmount(Player player) {
        int returnAmount = 1;
        if (player.getMainHandItem().is(Spirit.SOUL_STEEL_MAINHAND) || player.getOffhandItem().is(Spirit.SOUL_STEEL_OFFHAND))
            returnAmount++;
        return returnAmount + EnchantmentHelper.getEnchantmentLevel(SpiritMisc.SOUL_REAPER_ENCHANTMENT.get(), player);
    }

    public static boolean isAllowed(ItemStack crystal, TagKey<EntityType<?>> blacklistTag) {
        String entityTypeName = getSoulCrystalType(crystal);
        if(crystal.is(SpiritItems.SOUL_CRYSTAL.get()) && entityTypeName != null) {
            return EntityType.byString(entityTypeName).map(entityType -> !entityType.is(blacklistTag)).orElse(false);
        }
        return false;
    }

    public static boolean canCrystalBeUsedInCage(ItemStack crystal) {
        return isAllowed(crystal, Spirit.BLACKLISTED_TAG);
    }
}
