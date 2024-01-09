package earth.terrarium.spirit.compat.rei.categories;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.rituals.components.RitualComponent;
import earth.terrarium.spirit.common.registry.SpiritItems;
import earth.terrarium.spirit.compat.rei.displays.TransmutationDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class TransmutationRecipeCategory implements DisplayCategory<TransmutationDisplay> {
    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Spirit.MODID, "textures/gui/item_transmutation.png");
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "transmutation");
    public static final CategoryIdentifier<TransmutationDisplay> RECIPE = CategoryIdentifier.of(ID);

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(SpiritItems.SCYTHE.get());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("spirit.jei.soul_transmutation.title");
    }

    @Override
    public CategoryIdentifier<? extends TransmutationDisplay> getCategoryIdentifier() {
        return RECIPE;
    }

    @Override
    public int getDisplayWidth(TransmutationDisplay display) {
        return 149;
    }

    @Override
    public int getDisplayHeight() {
        return 88;
    }

    @Override
    public List<Widget> setupDisplay(TransmutationDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        var recipe = display.recipe();
        var startX = bounds.getCenterX() - getDisplayWidth(display) / 2;
        var startY = bounds.getCenterY() - getDisplayHeight() / 2;

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(GUI_BACKGROUND, startX, startY, getDisplayWidth(display), getDisplayHeight()));
        var totalComponents = recipe.inputs().size();
        var startAngle = Math.PI / 2;
        var range = 26;

        for (int i = 0; i < totalComponents; i++) {
            RitualComponent<?> ritualComponent = recipe.inputs().get(i);
            ritualComponent.getREIPlacer().placeWidget(startX + 38 + range * Math.cos(((double) i/totalComponents) * Math.PI * 2 - startAngle), startY + 35 + range * Math.sin(((double) i/totalComponents) * Math.PI * 2 - startAngle), display, bounds, widgets);
        }

        widgets.add(Widgets.createSlot(new Point(startX + 37, startY + 34)).disableBackground().entries(EntryIngredients.ofIngredient(recipe.catalyst())));
        recipe.result().getREIPlacer().placeWidget(startX + 116, startY + 34, display, bounds, widgets);
        return widgets;
    }
}
