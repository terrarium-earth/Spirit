package earth.terrarium.spirit.common.item.trinkets;

import earth.terrarium.spirit.common.util.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BaseTrinket extends Item {
    public static final String ENABLED_KEY = "Enabled";
    public BaseTrinket(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (player.isShiftKeyDown()) {
            if(level.isClientSide) return InteractionResultHolder.pass(player.getItemInHand(interactionHand));
            ItemStack itemStack = player.getItemInHand(interactionHand);
            itemStack.getOrCreateTag().putBoolean(ENABLED_KEY, !isEnabled(itemStack));
            player.displayClientMessage(Component.translatable("misc.spirit.trinket_" + (isEnabled(itemStack) ? "enabled" : "disabled")), true);
            return InteractionResultHolder.success(itemStack);
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.translatable("misc.spirit.trinket_" + (isEnabled(itemStack) ? "enabled" : "disabled")).withStyle(ChatFormatting.GRAY));
        var contents = Component.translatable(this.getDescriptionId() + ".desc").getString();
        List<MutableComponent> components = Arrays.stream(contents.split("<br>"))
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .map(Component::literal)
                .map(mutableComponent -> mutableComponent.withStyle(ChatFormatting.GRAY))
                .toList();
        ClientUtils.shiftTooltip(list, components, List.of());
    }

    //getter for the enabled state
    public static boolean isEnabled(ItemStack itemStack) {
        return !itemStack.hasTag() || itemStack.getTag().getBoolean(ENABLED_KEY);
    }

    public boolean isFoil(ItemStack itemStack) {
        return isEnabled(itemStack);
    }
}
