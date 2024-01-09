package earth.terrarium.spirit.compat.rei;

import earth.terrarium.spirit.api.rituals.components.impl.BasinComponent;
import earth.terrarium.spirit.api.rituals.components.impl.ItemComponent;
import earth.terrarium.spirit.api.rituals.components.impl.SoulComponent;
import earth.terrarium.spirit.api.rituals.results.impl.EntityResult;
import earth.terrarium.spirit.api.rituals.results.impl.ItemResult;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.rei.displays.TransmutationDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentUtils {

    public static ReiPlacer fluidPlacer(BasinComponent component) {
        return (x, y, display, bounds, widgets) -> {
            widgets.add(
                    Widgets.createSlot(new Point(x, y))
                            .markInput()
                            .disableBackground()
                            .entries(EntryIngredient.of(component.fluidIngredient().getFluids().stream().map(fluid -> EntryStacks.of(fluid.getFluid())).collect(Collectors.toList())))
            );
        };
    }

    public static ReiPlacer itemInputPlacer(ItemComponent component) {
        return (x, y, display, bounds, widgets) -> {
            widgets.add(
                    Widgets.createSlot(new Point(x, y))
                            .markInput()
                            .disableBackground()
                            .entries(EntryIngredients.ofIngredient(component.item()))
            );
        };
    }

    public static ReiPlacer soulInputPlacer(SoulComponent component) {
        return (x, y, display, bounds, widgets) -> {
            widgets.add(
                    Widgets.createSlot(new Point(x, y))
                            .markInput()
                            .disableBackground()
                            .entries(EntryIngredients.of(SpiritPlugin.ENTITY_INGREDIENT, component.soulIngredient().getEntities().map(entityType -> new EntityIngredient(entityType, -45)).collect(Collectors.toList())))
            );
        };
    }

    public static ReiPlacer itemOutputPlacer(ItemResult component) {
        return (x, y, display, bounds, widgets) -> {
            widgets.add(
                    Widgets.createSlot(new Point(x, y))
                            .disableBackground()
                            .markOutput()
                            .entries(EntryIngredients.of(component.item().copy()))
            );
        };
    }

    public static ReiPlacer soulOutputPlacer(EntityResult component) {
        return (x, y, display, bounds, widgets) -> {
            widgets.add(
                    Widgets.createSlot(new Point(x, y))
                            .disableBackground()
                            .markOutput()
                            .entries(EntryIngredients.of(SpiritPlugin.ENTITY_INGREDIENT, List.of(new EntityIngredient(component.entityType(), -45))))
            );
        };
    }

    @FunctionalInterface
    public interface ReiPlacer {
        void placeWidget(double x, double y, TransmutationDisplay display, Rectangle bounds, ArrayList<Widget> widgets);
    }
}
