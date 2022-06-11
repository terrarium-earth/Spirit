package me.codexadrian.spirit.utils;

import me.codexadrian.spirit.Corrupted;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.Tier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

public class SoulUtils {

    @Nullable
    public static Tier getTier(ItemStack itemStack) {
        if(!itemStack.hasTag() || !itemStack.getTag().contains("StoredEntity")) {
            return null;
        }
        int storedSouls = itemStack.getTag().getCompound("StoredEntity").getInt("Souls");
        String type = itemStack.getTag().getCompound("StoredEntity").getString("Type");
        Tier tier = null;
        for(Tier t : Spirit.getSpiritConfig().getTiers()) {
            if(Arrays.stream(t.getBlacklist()).noneMatch(b -> b.equals(type))) {
                if (t.getRequiredSouls() <= storedSouls) {
                    tier = t;
                } else {
                    break;
                }
            }
        }
        return tier;
    }

    public static int getTierIndex(ItemStack itemStack) {
        if(!itemStack.hasTag() || !itemStack.getTag().contains("StoredEntity")) {
            return -1;
        }
        int storedSouls = itemStack.getTag().getCompound("StoredEntity").getInt("Souls");
        String type = itemStack.getTag().getCompound("StoredEntity").getString("Type");
        int tier = 0;
        for(int i = 0; i < Spirit.getSpiritConfig().getTiers().length; i++) {
            Tier t = Spirit.getSpiritConfig().getTiers()[i];
            if(Arrays.stream(t.getBlacklist()).noneMatch(b -> b.equals(type))) {
                if (t.getRequiredSouls() <= storedSouls) {
                    tier = i;
                } else {
                    break;
                }
            }
        }
        return tier;
    }

    @Nullable
    public static Tier getNextTier(ItemStack itemStack) {
        if(!itemStack.hasTag() || !itemStack.getTag().contains("StoredEntity")) {
            return null;
        }
        int storedSouls = itemStack.getTag().getCompound("StoredEntity").getInt("Souls");
        String type = itemStack.getTag().getCompound("StoredEntity").getString("Type");
        Tier tier = null;
        for(Tier t : Spirit.getSpiritConfig().getTiers()) {
            if(Arrays.stream(t.getBlacklist()).noneMatch(b -> b.equals(type))) {
                if (t.getRequiredSouls() > storedSouls) {
                    tier = t;
                    break;
                }
            }
        }
        return tier;
    }

    public static int getMaxSouls(ItemStack itemStack) {
        if(!itemStack.hasTag() || !itemStack.getTag().contains("StoredEntity")) {
            return Integer.MAX_VALUE;
        }
        String type = itemStack.getTag().getCompound("StoredEntity").getString("Type");
        int requiredSouls = 0;
        for(int i = 0; i < Spirit.getSpiritConfig().getTiers().length; i++) {
            Tier t = Spirit.getSpiritConfig().getTiers()[i];
            if(Arrays.stream(t.getBlacklist()).noneMatch(b -> b.equals(type))) {
                if(requiredSouls < t.getRequiredSouls()) {
                    requiredSouls = t.getRequiredSouls();
                }
            }
        }
        return requiredSouls;
    }

    public static boolean isMaxTier(ItemStack itemStack) {
        Tier tier = getTier(itemStack);
        Tier nextTier = getNextTier(itemStack);
        if(tier == null || nextTier == null) return false;

        return tier == nextTier;
    }

    public static boolean canCrystalAcceptSoul(ItemStack crystal, LivingEntity victim) {
        if(crystal.is(SpiritRegistry.SOUL_CRYSTAL.get())) {
            boolean isEmpty = !crystal.hasTag();
            if(!isEmpty) {
                boolean isCorrectType = crystal.getTag().getCompound("StoredEntity").getString("Type").equals(Registry.ENTITY_TYPE.getKey(victim.getType()).toString());
                boolean hasRoomForMore = crystal.getTag().getCompound("StoredEntity").getInt("Souls") < SoulUtils.getMaxSouls(crystal);
                return isCorrectType && hasRoomForMore;
            } else return true;
        } else if(crystal.is(SpiritRegistry.CRUDE_SOUL_CRYSTAL.get())) {
            if(crystal.hasTag() && crystal.getTag().contains("Souls")) {
                return crystal.getTag().getInt("Souls") < Spirit.getSpiritConfig().getCrudeSoulCrystalCap();
            } else return true;
        } else return false;
    }

    public static float getActivation(ItemStack stack) {
        Tier tier = SoulUtils.getTier(stack);
        if (tier == null) {
            return 0f;
        }

        return ((float) tier.getRequiredSouls()) / SoulUtils.getMaxSouls(stack);
    }

    public static ItemStack getSoulCrystal(Player player, LivingEntity victim) {
        Corrupted corrupt = (Corrupted) victim;
        ItemStack savedStack = ItemStack.EMPTY;
        int savedSouls = 0;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack currentItem = player.getInventory().getItem(i);
            if (currentItem.getItem() != SpiritRegistry.SOUL_CRYSTAL.get()) {
                continue;
            }

            if (savedStack.isEmpty() && canCrystalAcceptSoul(currentItem, victim)) {
                savedStack = currentItem;
            } else {
                if (currentItem.hasTag()) {
                    //if the current savedStack either is empty or has some amount of souls in it. therefore any new crystal that's empty
                    //is either equal to or worse than the saved stack, so the current item, if it is empty, should be skipped.
                    CompoundTag tag = currentItem.getTag().getCompound("StoredEntity");
                    if (tag.getString("Type").equals(Registry.ENTITY_TYPE.getKey(victim.getType()).toString()) && tag.getInt("Souls") < SoulUtils.getMaxSouls(currentItem)) {
                        int souls = tag.getInt("Souls");
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

    public static ItemStack getCrudeSoulCrystal(Player player) {
        ItemStack savedStack = ItemStack.EMPTY;
        int savedSouls = 0;

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack currentItem = player.getInventory().getItem(i);
            if (currentItem.getItem() != SpiritRegistry.CRUDE_SOUL_CRYSTAL.get()) {
                continue;
            }

            if((currentItem.hasTag() && currentItem.getTag().getInt("Souls") >= SoulUtils.getMaxSouls(currentItem))) continue;

            if (savedStack.isEmpty()) {
                savedStack = currentItem;
                if(currentItem.hasTag() && currentItem.getTag().contains("Souls")) savedSouls = savedStack.getTag().getInt("Souls");
            } else if(currentItem.hasTag() && currentItem.getTag().contains("Souls")){
                CompoundTag tag = currentItem.getTag();
                //if the current savedStack either is empty or has some amount of souls in it. therefore any new crystal that's empty
                //is either equal to or worse than the saved stack, so the current item, if it is empty, should be skipped.
                if(tag.getInt("Souls") > savedSouls) {
                    savedStack = currentItem;
                    savedSouls = tag.getInt("Souls");
                }
            }
        }

        return savedStack;
    }

    public static void handleSoulCrystal(ItemStack soulCrystal, Player player, LivingEntity victim) {
        CompoundTag storedEntity;

        if (!soulCrystal.hasTag() || !soulCrystal.getTag().contains("StoredEntity")) {
            CompoundTag tag = new CompoundTag();
            tag.putString("Type", Registry.ENTITY_TYPE.getKey(victim.getType()).toString());
            soulCrystal.getOrCreateTag().put("StoredEntity", tag);
            storedEntity = tag;
        } else {
            storedEntity = soulCrystal.getTag().getCompound("StoredEntity");
        }

        ServerLevel serverLevel = (ServerLevel) player.level;
        serverLevel.sendParticles(ParticleTypes.SOUL, victim.getX(), victim.getY(), victim.getZ(), 20, victim.getBbWidth(), victim.getBbHeight(), victim.getBbWidth(), 0);
        Tier tier = SoulUtils.getNextTier(soulCrystal);

        int incrementAmount = getSoulHarvestAmount(player);

        if (tier != null && storedEntity.getInt("Souls") + incrementAmount >= tier.getRequiredSouls()) {
            player.displayClientMessage(Component.translatable("item.spirit.soul_crystal.upgrade_message").withStyle(ChatFormatting.AQUA), true);
            serverLevel.sendParticles(ParticleTypes.SOUL, player.getX(), player.getY(), player.getZ(), 40, 1, 2, 1, 0);
        }

        storedEntity.putInt("Souls", storedEntity.getInt("Souls") + incrementAmount);
    }

    public static void handleCrudeSoulCrystal(ItemStack crudeCrystal, Player player, LivingEntity victim) {
        int soulCount;
        if(!crudeCrystal.hasTag() || !crudeCrystal.getTag().contains("Souls")) {
            soulCount = 0;
        } else {
            soulCount = crudeCrystal.getTag().getInt("Souls");
        }
        ServerLevel serverLevel = (ServerLevel) player.level;
        serverLevel.sendParticles(ParticleTypes.SOUL, victim.getX(), victim.getY(), victim.getZ(), 20, victim.getBbWidth(), victim.getBbHeight(), victim.getBbWidth(), 0);
        crudeCrystal.getOrCreateTag().putInt("Souls", soulCount + getSoulHarvestAmount(player));
    }

    public static int getSoulHarvestAmount(Player player) {
        int returnAmount = 1;
        ItemStack stack = player.getMainHandItem();
        if(stack.is(SpiritRegistry.SOUL_BLADE.get())) returnAmount += 1;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if(enchantments.containsKey(SpiritRegistry.SOUL_REAPER_ENCHANTMENT.get())) {
           returnAmount += enchantments.get(SpiritRegistry.SOUL_REAPER_ENCHANTMENT.get());
        }
        return returnAmount;
    }
}
