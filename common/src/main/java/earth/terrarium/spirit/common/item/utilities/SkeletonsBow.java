package earth.terrarium.spirit.common.item.utilities;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SkeletonsBow extends BowItem {
    public static final String ARROW = "ArrowStorage";

    public SkeletonsBow(Properties properties) {
        super(properties);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int i) {
        boolean bl2;
        int j;
        float f;
        if (!(livingEntity instanceof Player player)) {
            return;
        }
        boolean bl = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, itemStack) > 0;
        ItemStack itemStack2 = getContents(itemStack).findFirst().orElse(ItemStack.EMPTY);
        if (itemStack2.isEmpty() && !bl) {
            return;
        }
        if (itemStack2.isEmpty()) {
            itemStack2 = new ItemStack(Items.ARROW);
        }
        if ((double)(f = BowItem.getPowerForTime(j = this.getUseDuration(itemStack) - i)) < 0.1) {
            return;
        }
        boolean bl3 = bl2 = bl && itemStack2.is(Items.ARROW);
        if (!level.isClientSide) {
            int l;
            int k;
            ArrowItem arrowItem = (ArrowItem)(itemStack2.getItem() instanceof ArrowItem ? itemStack2.getItem() : Items.ARROW);
            AbstractArrow abstractArrow = arrowItem.createArrow(level, itemStack2, player);
            abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f, f * 3.0f, 1.0f);
            if (f == 1.0f) {
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
            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            level.addFreshEntity(abstractArrow);
        }
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + f * 0.5f);
        player.awardStat(Stats.ITEM_USED.get(this));
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) {
        if (clickAction != ClickAction.SECONDARY) {
            return false;
        }
        ItemStack itemStack22 = slot.getItem();
        if (itemStack22.isEmpty()) {
            this.playRemoveOneSound(player);
            removeOne(itemStack).ifPresent(slot::safeInsert);
        } else if (itemStack22.getItem().canFitInsideContainerItems()) {
            int j = add(itemStack, itemStack22);
            if (j > 0) {
                this.playInsertSound(player);
                itemStack22.shrink(j);
            }
        }
        return true;
    }

    private static int add(ItemStack itemStack, ItemStack itemStack2) {
        if (itemStack2.isEmpty() || !itemStack2.getItem().canFitInsideContainerItems()) {
            return 0;
        }

        if (!(itemStack.getItem() instanceof ProjectileWeaponItem projectile) || !projectile.getAllSupportedProjectiles().test(itemStack2)) {
            return 0;
        }

        if (itemStack2.getCount() > 0 && !itemStack.getOrCreateTag().contains(ARROW)) {
            itemStack.addTagElement(ARROW, itemStack2.save(new CompoundTag()));
            return itemStack2.getCount();
        }

        return 0;
    }

    private static Optional<ItemStack> removeOne(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains(ARROW)) {
            return Optional.empty();
        }
        ItemStack stack = ItemStack.of(compoundTag.getCompound(ARROW));
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        itemStack.removeTagKey(ARROW);
        return Optional.of(stack);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        boolean bl;
        ItemStack itemStack = player.getItemInHand(interactionHand);
        boolean bl2 = bl = !getContents(itemStack).findFirst().orElse(ItemStack.EMPTY).isEmpty();
        if (player.getAbilities().instabuild || bl) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    private static Stream<ItemStack> getContents(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return Stream.empty();
        }
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (!compoundTag.contains(ARROW)) {
            return Stream.empty();
        }
        ItemStack itemStack2 = ItemStack.of(compoundTag.getCompound(ARROW));
        if (itemStack2.isEmpty()) {
            return Stream.empty();
        }
        return Stream.of(itemStack2);
    }

    @Override
    public @NotNull Optional<TooltipComponent> getTooltipImage(ItemStack itemStack) {
        NonNullList<ItemStack> nonNullList = NonNullList.create();
        ItemStack inventory = ItemStack.of(itemStack.getOrCreateTag().getCompound(ARROW));
        nonNullList.add(inventory);
        return Optional.of(new BundleTooltip(nonNullList, inventory.getCount()));
    }

    @Override
    public void onDestroyed(ItemEntity itemEntity) {
        ItemUtils.onContainerDestroyed(itemEntity, getContents(itemEntity.getItem()));
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_DROP_CONTENTS, 0.8f, 0.8f + entity.level().getRandom().nextFloat() * 0.4f);
    }
}
