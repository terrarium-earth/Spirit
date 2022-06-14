package me.codexadrian.spirit.items;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import me.codexadrian.spirit.recipe.SoulArrowEffect;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SoulBowItem extends BowItem {
    public SoulBowItem(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity, int drawTime) {
        if (livingEntity instanceof Player player) {
            float power;
            boolean isCreative = player.getAbilities().instabuild;
            ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true);
            if ((soulCrystal.isEmpty() && SoulUtils.getSoulsInCrystal(soulCrystal) == 0) || !isCreative) {
                return;
            }
            if ((double)(power = BowItem.getPowerForTime(this.getUseDuration(itemStack) - drawTime)) < 0.1) {
                return;
            }
            if (!level.isClientSide) {
                int l;
                int k;
                SoulArrowEntity abstractArrow = SpiritRegistry.SOUL_ARROW_ENTITY.get().create(level);
                if(abstractArrow == null) return;
                abstractArrow.setOwner(player);
                abstractArrow.setPos(player.getEyePosition());
                abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, power * 3.0f, 1.0f);
                if(soulCrystal.is(SpiritRegistry.SOUL_CRYSTAL.get())) {
                    var arrowEffect = SoulArrowEffect.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(Objects.requireNonNull(SoulUtils.getSoulCrystalType(soulCrystal)))), level.getRecipeManager());
                    if(arrowEffect.isPresent()) {
                        abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + arrowEffect.get().additionalDamage());
                        if(arrowEffect.get().isOnFire()) {
                            abstractArrow.setSecondsOnFire(arrowEffect.get().burnTime());
                        }
                        Optional<List<MobEffectInstance>> potionEffects = arrowEffect.get().potionEffects();
                        if(potionEffects.isPresent()) {
                            for(var effect : potionEffects.get()) {
                                abstractArrow.addEffect(new MobEffectInstance(effect));
                            }
                        }
                    }
                }
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
            if (!isCreative) {
                SoulUtils.deviateSoulCount(soulCrystal, -1, level, null);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        boolean bl;
        ItemStack itemStack = player.getItemInHand(interactionHand);
        bl = !SoulUtils.findCrystal(player, null, true).isEmpty();
        if (player.getAbilities().instabuild || bl) {
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
