package me.codexadrian.spirit.items.tools;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.registry.SpiritMisc;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SoulSteelBow extends BowItem {
    public SoulSteelBow(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int drawTime) {
        if (livingEntity instanceof Player player) {
            float power;
            boolean isCreative = player.getAbilities().instabuild;
            ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true);
            if ((soulCrystal.isEmpty() || SoulUtils.getSoulsInCrystal(soulCrystal) == 0) && !isCreative) {
                return;
            }
            if ((double) (power = BowItem.getPowerForTime(this.getUseDuration(itemStack) - drawTime)) < 0.1) {
                return;
            }
            if (!level.isClientSide) {
                int l;
                int k;
                SoulArrowEntity soulArrow = SpiritMisc.SOUL_ARROW_ENTITY.get().create(level);
                if (soulArrow == null) return;
                soulArrow.setOwner(player);
                soulArrow.setPos(player.getEyePosition());
                soulArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, power * 3.0f, 1.0f);
                if (soulCrystal.is(SpiritItems.SOUL_CRYSTAL.get())) {
                    var arrowEffect = MobTraitData.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(Objects.requireNonNull(SoulUtils.getSoulCrystalType(soulCrystal)))), level.getRecipeManager());
                    arrowEffect.ifPresent(soulArrow::addArrowEffect);
                }
                if (power == 1.0f) {
                    soulArrow.setCritArrow(true);
                }
                if ((k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack)) > 0) {
                    soulArrow.setBaseDamage(soulArrow.getBaseDamage() + (double) k * 0.5 + 0.5);
                }
                if ((l = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack)) > 0) {
                    soulArrow.setKnockback(l);
                }
                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) > 0) {
                    soulArrow.setSecondsOnFire(100);
                }
                itemStack.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(player.getUsedItemHand()));
                soulArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                level.addFreshEntity(soulArrow);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f);
            if (!isCreative) {
                SoulUtils.deviateSoulCount(soulCrystal, -1, level, null);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        ItemStack crystal = SoulUtils.findCrystal(player, null, true);
        if (player.getAbilities().instabuild || (!crystal.isEmpty() && SoulUtils.getSoulsInCrystal(crystal) > 0)) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return Spirit.SOUL_COLOR;
    }
}
