package me.codexadrian.spirit.items;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SoulSteelWand extends Item {
    public SoulSteelWand(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (player.isShiftKeyDown()) {
            cycleMode(player, interactionHand, true);
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        }
        return super.use(level, player, interactionHand);
    }

    public static void cycleMode(Player player, InteractionHand hand, boolean forward) {
        ItemStack stack = player.getItemInHand(hand);
        int index = getMode(stack);
        int increment = forward ? 1 : -1;
        int mode = index + increment >= 0 ? (index + increment) % 10 : 9;
        stack.getOrCreateTag().putInt("ModeIndex", mode);
        if(mode == 0) {
            player.displayClientMessage(Component.translatable("item.spirit.soul_wand_transmute_message").withStyle(ChatFormatting.DARK_PURPLE), true);
        } else {
            player.displayClientMessage(Component.translatable("item.spirit.soul_wand_soul_transfer_message", Component.literal(String.valueOf(Math.pow(2, mode))).withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.DARK_AQUA), true);
        }
    }

    public static int getMode(ItemStack stack) {
        if(stack.hasTag()) {
            return stack.getOrCreateTag().getInt("ModeIndex");
        }
        return 0;
    }
}
