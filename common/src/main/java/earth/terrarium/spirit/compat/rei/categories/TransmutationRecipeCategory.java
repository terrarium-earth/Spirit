package earth.terrarium.spirit.compat.rei.categories;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.utils.SoulUtils;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.rei.SpiritPlugin;
import earth.terrarium.spirit.compat.rei.displays.SummoningDisplay;
import earth.terrarium.spirit.compat.rei.displays.TransmutationDisplay;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransmutationRecipeCategory implements DisplayCategory<TransmutationDisplay> {
    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Spirit.MODID, "textures/gui/soul_transmutation.png");
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "transmutation");
    public static final CategoryIdentifier<TransmutationDisplay> RECIPE = CategoryIdentifier.of(ID);

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(SpiritBlocks.TRANSMUTATION_PEDESTAL.get().asItem().getDefaultInstance());
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
        return 150 + 8;
    }

    @Override
    public int getDisplayHeight() {
        return 100 + 8;
    }

    @Override
    public List<Widget> setupDisplay(TransmutationDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));
        var startX = bounds.getCenterX() - 150 / 2;
        var startY = bounds.getCenterY() - 100 / 2;
        widgets.add(Widgets.createTexturedWidget(GUI_BACKGROUND, startX, startY, 150, 100));
        var recipe = display.recipe();
        var totalComponents = recipe.itemInputs().size() + recipe.entityInputs().size();
        var startAngle = Math.PI / 2;

        for (int i = 0; i < recipe.itemInputs().size(); i++) {
            widgets.add(
                    Widgets.createSlot(new Point(startX + 32 + 32 * Math.cos(((double) i/totalComponents) * Math.PI * 2 - startAngle), startY + 40 + 32 * Math.sin(((double) i/totalComponents) * Math.PI * 2 - startAngle)))
                            .markInput()
                            .disableBackground()
                            .entries(EntryIngredients.ofIngredient(recipe.itemInputs().get(i)))
            );
        }

        var nbt = new CompoundTag();
        nbt.putBoolean(SoulUtils.SOULLESS_TAG, true);
        for (int i = recipe.itemInputs().size(); i < recipe.itemInputs().size() + recipe.entityInputs().size(); i++) {
            var entityTypes = recipe.entityInputs().get(i - recipe.itemInputs().size()).getEntities().map(entityType -> new EntityIngredient(entityType, -45F, Optional.of(nbt))).toList();
            widgets.add(
                    Widgets.createSlot(new Point(startX + 32 + 32 * Math.cos(((double) i/totalComponents) * Math.PI * 2 - startAngle), startY + 40 + 32 * Math.sin(((double) i/totalComponents) * Math.PI * 2 - startAngle)))
                            .markInput()
                            .disableBackground()
                            .entries(EntryIngredients.of(SpiritPlugin.ENTITY_INGREDIENT, entityTypes))
            );
        }

        if(recipe.activationItem().isPresent()) {
            widgets.add(Widgets.createSlot(new Point(startX + 32, startY + 40)).disableBackground().entries(EntryIngredients.ofIngredient(recipe.activationItem().get()).map(stack -> {
                if (recipe.consumesActivator()) {
                    stack.tooltip(Component.translatable("spirit.jei.soul_transmutation.consumes").withStyle(ChatFormatting.RED));
                }

                return stack;
            })));
        } else {
            widgets.add(Widgets.createTooltip(new Rectangle(startX + 32, startY + 40, 18, 18), Component.translatable("spirit.jei.soul_transmutation.empty_hand")));
        }
        widgets.add(Widgets.createSlot(new Point(startX + 124, startY + 40)).markOutput().disableBackground().entry(EntryStacks.of(recipe.result())));
        return widgets;
    }
}
