package earth.terrarium.spirit.api.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class TooltipUtils {
    private static final List<Tooltip> shiftTooltips = new ArrayList<>();
    private static final List<Tooltip> normalTooltips = new ArrayList<>();

    private TooltipUtils() {
    }

    public static void addShiftTooltip(Tooltip tooltip) {
        shiftTooltips.add(tooltip);
    }

    public static void addNormalTooltip(Tooltip tooltip) {
        normalTooltips.add(tooltip);
    }

    public static List<Tooltip> getShiftTooltips() {
        return shiftTooltips;
    }

    public static List<Tooltip> getNormalTooltips() {
        return normalTooltips;
    }

    @FunctionalInterface
    public interface Tooltip {
        void addTooltip(List<Component> components, ItemStack stack, Level level);
    }
}
