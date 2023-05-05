package earth.terrarium.spirit.compat.rei.categories;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.utils.SoulUtils;
import earth.terrarium.spirit.common.registry.SpiritItems;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.rei.SpiritPlugin;
import earth.terrarium.spirit.compat.rei.displays.InfusionDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static earth.terrarium.spirit.compat.rei.SpiritPlugin.setupPedestal;

public class InfusionRecipeCategory implements DisplayCategory<InfusionDisplay> {
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "infusion");
    public static final CategoryIdentifier<InfusionDisplay> RECIPE = CategoryIdentifier.of(ID);

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(SpiritItems.SCYTHE.get());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("spirit.jei.soul_transmutation.title");
    }

    @Override
    public CategoryIdentifier<? extends InfusionDisplay> getCategoryIdentifier() {
        return RECIPE;
    }

    @Override
    public int getDisplayWidth(InfusionDisplay display) {
        return 149;
    }

    @Override
    public int getDisplayHeight() {
        return 88;
    }

    @Override
    public List<Widget> setupDisplay(InfusionDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        var recipe = display.recipe();
        setupPedestal(display, bounds, getDisplayWidth(display), getDisplayHeight(), widgets, recipe);
        var startX = bounds.getCenterX() - getDisplayWidth(display) / 2;
        var startY = bounds.getCenterY() - getDisplayHeight() / 2;

        widgets.add(Widgets.createSlot(new Point(startX + 37, startY + 34)).disableBackground().entries(EntryIngredients.ofIngredient(recipe.activationItem())));
        widgets.add(Widgets.createSlot(new Point(startX + 116, startY + 34)).markOutput().disableBackground().entries(InfusionDisplay.getOuputs(display.recipe())));
        return widgets;
    }
}
