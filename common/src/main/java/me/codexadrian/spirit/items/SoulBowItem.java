package me.codexadrian.spirit.items;

import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class SoulBowItem extends BowItem {
    public SoulBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int drawTime) {
        if (livingEntity instanceof Player player) {
            float power;
            boolean isCreative = player.getAbilities().instabuild;
            ItemStack projectile = SoulUtils.getCrudeSoulCrystal(player);
            if ((projectile.isEmpty() || SoulUtils.getSoulsInCrystal(projectile) == 0) && !isCreative) {
                return;
            }
            if ((double)(power = BowItem.getPowerForTime(this.getUseDuration(itemStack) - drawTime)) < 0.1) {
                return;
            }
            if (!level.isClientSide) {
                int l;
                int k;
                AbstractArrow abstractArrow = SpiritRegistry.SOUL_ARROW_ENTITY.get().create(level);
                if(abstractArrow == null) return;
                abstractArrow.setOwner(player);
                abstractArrow.setPos(player.getEyePosition());
                abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, power * 3.0f, 1.0f);
                if (power == 1.0f) {
                    abstractArrow.setCritArrow(true);
                }
                if ((k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack)) > 0) {
                    abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + (double)k * 0.5 + 0.5);
                }
                if ((l = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack)) > 0) {
                    abstractArrow.setKnockback(l);
                }
                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack) > 0) {
                    abstractArrow.setSecondsOnFire(100);
                }
                itemStack.hurtAndBreak(1, player, player2 -> player2.broadcastBreakEvent(player.getUsedItemHand()));
                abstractArrow.pickup = AbstractArrow.Pickup.DISALLOWED;
                level.addFreshEntity(abstractArrow);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f);
            if (!player.getAbilities().instabuild) {
                SoulUtils.decrementSoulCount(projectile, 1);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        boolean bl;
        ItemStack itemStack = player.getItemInHand(interactionHand);
        bl = !SoulUtils.getCrudeSoulCrystal(player).isEmpty();
        if (player.getAbilities().instabuild || bl) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        return 0x00fffb;
    }
}
