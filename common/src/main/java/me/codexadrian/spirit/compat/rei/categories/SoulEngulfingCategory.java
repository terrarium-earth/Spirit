package me.codexadrian.spirit.compat.rei.categories;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.compat.jei.multiblock.SoulEngulfingRecipeWrapper;
import me.codexadrian.spirit.compat.rei.displays.SoulEngulfingDisplay;
import me.codexadrian.spirit.registry.SpiritItems;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.*;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SoulEngulfingCategory implements DisplayCategory<SoulEngulfingDisplay> {

    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Spirit.MODID, "textures/gui/soul_engulfing.png");
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "soul_engulfing");
    public static final CategoryIdentifier<SoulEngulfingDisplay> RECIPE = CategoryIdentifier.of(ID);
    private static final double OFFSET = Math.sqrt(512) * .5;
    public long lastTime;
    private final BlockRenderDispatcher dispatcher;

    public SoulEngulfingCategory() {
        lastTime = System.currentTimeMillis();
        dispatcher = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(SpiritItems.SOUL_CRYSTAL.get().getDefaultInstance());
    }

    @Override
    public Component getTitle() {
        return Component.translatable("spirit.jei.soul_engulfing.title");
    }

    @Override
    public CategoryIdentifier<? extends SoulEngulfingDisplay> getCategoryIdentifier() {
        return RECIPE;
    }

    @Override
    public int getDisplayWidth(SoulEngulfingDisplay display) {
        return 150 + 8;
    }

    @Override
    public int getDisplayHeight() {
        return 100 + 8;
    }

    @Override
    public List<Widget> setupDisplay(SoulEngulfingDisplay display, Rectangle bounds) {
        var widgets = new ArrayList<Widget>();
        widgets.add(Widgets.createRecipeBase(bounds));
        var startX = bounds.getCenterX() - 150 / 2;
        var startY = bounds.getCenterY() - 100 / 2;
        widgets.add(Widgets.createTexturedWidget(GUI_BACKGROUND, startX, startY, 150, 100));
        widgets.add(Widgets.createSlot(new Point(startX + 2, startY + 2)).markInput().entries(display.getInput()));
        widgets.add(Widgets.createSlot(new Point(startX + 133, startY + 83)).markOutput().entries(display.getOutput()));
        widgets.add(new DelegateWidget(Widgets.noOp()) {
            @Override
            public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
                stack.pushPose();
                stack.translate(startX, startY, 0);
                draw(display.getWrapper(), stack, mouseX, mouseY);
                stack.popPose();
                List<Component> strings = getTooltipStrings(display.getWrapper(), mouseX - startX, mouseY - startY);
                if (!strings.isEmpty()) {
                    Tooltip.create(new Point(mouseX, mouseY), strings).queue();
                }
            }

            public Rectangle getBounds() {
                return new Rectangle(2, 26, 103, 74);
            }

            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                return handleInput(display.getWrapper(), keyCode, scanCode);
            }
        });
        return widgets;
    }

    public List<Component> getTooltipStrings(SoulEngulfingRecipeWrapper recipe, double mouseX, double mouseY) {
        List<Component> components = new ArrayList<>();
        var tempBlockMap = new ArrayList<>(recipe.blockMap);
        Collections.reverse(tempBlockMap);
        if (mouseX > 1 && mouseX < 105 && mouseY > 27 && mouseY < 99) {
            for (int i = 0; i < tempBlockMap.size(); i++) {
                components.add(Component.translatable("spirit.jei.soul_engulfing.layer", (i + 1)).withStyle(ChatFormatting.DARK_GRAY));
                for (SoulEngulfingRecipeWrapper.BlockMap blockMap : tempBlockMap.get(i)) {
                    components.add(Component.literal("  ").append(blockMap.blocks().getCurrent().getName()).withStyle(ChatFormatting.GRAY));
                }
            }
            if (recipe.getRecipe().breaksBlocks())
                components.add(Component.translatable("spirit.jei.soul_engulfing.consumes").withStyle(ChatFormatting.RED));
        }
        if (mouseX > 107 && mouseX < 129 && mouseY > 83 && mouseY < 98) {
            components.add(Component.translatable("spirit.jei.soul_engulfing.duration", recipe.getRecipe().duration() * 0.05));
        }
        return components;
    }

    public void draw(SoulEngulfingRecipeWrapper recipe, PoseStack stack, double mouseX, double mouseY) {
        long l = System.currentTimeMillis();

        if (lastTime + 1500 <= l && !Screen.hasShiftDown()) {
            recipe.tick();
            lastTime = l;
        }
        double guiScale = Minecraft.getInstance().getWindow().getGuiScale();
        try (CloseableScissors ignored = Widget.scissor(stack, new Rectangle(2, 26, 103, 74))) {
            stack.pushPose();
            Lighting.setupForFlatItems();
            stack.translate(52 - recipe.getMultiblock().pattern().get(0).size() * OFFSET, recipe.blockMap.size() * 16 + (66 - recipe.blockMap.size() * OFFSET), 100);
            stack.scale(16F, -16F, 1);
            stack.mulPose(Vector3f.XP.rotationDegrees(45));
            stack.mulPose(Vector3f.YP.rotationDegrees(45));
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            for (int i = 0; i < Math.min(recipe.blockMap.size(), recipe.layer); i++) {
                for (SoulEngulfingRecipeWrapper.BlockMap blockMap : recipe.blockMap.get(i)) {
                    stack.pushPose();
                    stack.translate(blockMap.pos().getX(), blockMap.pos().getY(), blockMap.pos().getZ());
                    dispatcher.renderSingleBlock(blockMap.blocks().getCurrent().defaultBlockState(), stack, bufferSource, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
                    stack.popPose();
                }
            }
            bufferSource.endBatch();
            stack.popPose();
            Lighting.setupFor3DItems();
        }
    }

    public boolean handleInput(SoulEngulfingRecipeWrapper recipe, int keyCode, int scanCode) {
        if (keyCode == InputConstants.KEY_UP) {
            recipe.layer = Math.min(recipe.layer + 1, recipe.blockMap.size());
            return true;
        }
        if (keyCode == InputConstants.KEY_DOWN) {
            recipe.layer = Math.max(recipe.layer - 1, 0);
            return true;
        }
        return false;
    }
}
