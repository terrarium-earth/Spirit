package me.codexadrian.spirit.compat.jei.categories;
/*
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3d;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.recipe.PedestalRecipe;
import me.codexadrian.spirit.recipe.SoulEngulfingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SoulEngulfingCategory extends BaseCategory<SoulEngulfingRecipe> {

    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Spirit.MODID, "textures/gui/soul_transmutation.png");
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "soul_transmutation");
    public static final RecipeType<PedestalRecipe> RECIPE = new RecipeType<>(ID, PedestalRecipe.class);
    protected SoulEngulfingCategory(IGuiHelper guiHelper, RecipeType<SoulEngulfingRecipe> recipeType, Component localizedName, IDrawable background, IDrawable icon) {
        super(guiHelper, recipeType, localizedName, background, icon);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, SoulEngulfingRecipe recipe, @NotNull IFocusGroup focuses) {
        if (recipe.input().multiblock().isPresent()) {
            List<ItemStack> items = new ArrayList<>();
            var blocks = recipe.input().multiblock().get().keys().values();
            var holderSets = blocks.stream().flatMap(predicate -> {
                if(predicate.blocks().isPresent()) return predicate.blocks().get().stream();
                return Stream.of();
            }).toList();
            for (Holder<Block> holderSet : holderSets) {
                items.add(holderSet.value().asItem().getDefaultInstance());
            }
            builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST).addIngredients(Ingredient.of(items.stream()));
        }
    }

    //region Rendering help
    private void drawScaledTexture(
            PoseStack matrixStack,
            ResourceLocation texture,
            ScreenArea area,
            float u, float v,
            int uWidth, int vHeight,
            int textureWidth, int textureHeight) {

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

        RenderSystem.enableDepthTest();
        GuiComponent.blit(matrixStack, area.x, area.y, area.width, area.height, u, v, uWidth, vHeight, textureWidth, textureHeight);
    }

    @Override
    public void draw(SoulEngulfingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        AABB dims = new AABB();

        Window mainWindow = Minecraft.getInstance().getWindow();

        drawScaledTexture(stack,
                new ResourceLocation(Spirit.MODID, "textures/gui/jei-arrow-field.png"),
                new ScreenArea(7, 20, 17, 22),
                0, 0, 17, 22, 17, 22);

        drawScaledTexture(stack,
                new ResourceLocation(Spirit.MODID, "textures/gui/jei-arrow-outputs.png"),
                new ScreenArea(100, 25, 24, 19),
                0, 0, 24, 19, 24, 19);

        int scissorX = 27;
        int scissorY = 0;

        double guiScaleFactor = mainWindow.getGuiScale();
        ScreenArea scissorBounds = new ScreenArea(
                scissorX, scissorY,
                70,
                70
        );

        renderPreviewControls(mx, dims);

        if (previewLevel != null) renderRecipe(recipe, mx, dims, guiScaleFactor, scissorBounds);
    }

    private void renderRecipe(MiniaturizationRecipe recipe, PoseStack mx, AABB dims, double guiScaleFactor, ScreenArea scissorBounds) {
        try {
            GuiComponent.fill(
                    mx,
                    scissorBounds.x, scissorBounds.y,
                    scissorBounds.x + scissorBounds.width,
                    scissorBounds.height,
                    0xFF404040
            );

            MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();

            final double scale = Minecraft.getInstance().getWindow().getGuiScale();
            final Matrix4f matrix = mx.last().pose();
            final FloatBuffer buf = BufferUtils.createFloatBuffer(16);
            matrix.store(buf);

            // { x, y, z }
            Vec3 translation = new Vec3(
                    buf.get(12) * scale,
                    buf.get(13) * scale,
                    buf.get(14) * scale);

            scissorBounds.x *= scale;
            scissorBounds.y *= scale;
            scissorBounds.width *= scale;
            scissorBounds.height *= scale;
            final int scissorX = Math.round(Math.round(translation.x + scissorBounds.x));
            final int scissorY = Math.round(Math.round(Minecraft.getInstance().getWindow().getHeight() - scissorBounds.y - scissorBounds.height - translation.y));
            final int scissorW = Math.round(scissorBounds.width);
            final int scissorH = Math.round(scissorBounds.height);
            RenderSystem.enableScissor(scissorX, scissorY, scissorW, scissorH);

            mx.pushPose();

            mx.translate(
                    27 + (35),
                    scissorBounds.y + (35),
                    100);

            // 13 = 1
            // 11 = 3
            // 09 = 5
            // 07 = 7
            // 05 = 9
            // 03 = 11
            // 01 = 13

            Vec3 dimsVec = new Vec3(dims.getXsize(), dims.getYsize(), dims.getZsize());
            float recipeAvgDim = (float) dimsVec.length();
            float previewScale = (float) ((3 + Math.exp(3 - (recipeAvgDim / 5))) / explodeMulti);
            mx.scale(previewScale, -previewScale, previewScale);

            drawActualRecipe(recipe, mx, dims, buffers);

            mx.popPose();

            buffers.endBatch();

            RenderSystem.disableScissor();
        } catch (Exception ex) {
            CompactCrafting.LOGGER.warn(ex);
        }
    }

    private void drawActualRecipe(MiniaturizationRecipe recipe, PoseStack mx, AABB dims, MultiBufferSource.BufferSource buffers) {
        double gameTime = Minecraft.getInstance().level.getGameTime();
        double test = Math.toDegrees(gameTime) / 15;
        mx.mulPose(new Quaternion(35f,
                (float) -test,
                0,
                true));

        double ySize = recipe.getDimensions().getYsize();

        // Variable explode based on mouse position (clamped)
        // double explodeMulti = MathHelper.clamp(mouseX, 0, this.background.getWidth())/this.background.getWidth()*2+1;


        int[] renderLayers;
        if (!singleLayer) {
            renderLayers = IntStream.range(0, (int) ySize).toArray();
        } else {
            renderLayers = new int[]{singleLayerOffset};
        }

        mx.translate(
                -(dims.getXsize() / 2) * explodeMulti - 0.5,
                -(dims.getYsize() / 2) * explodeMulti - 0.5,
                -(dims.getZsize() / 2) * explodeMulti - 0.5
        );

        for (int y : renderLayers) {
            recipe.getLayer(y).ifPresent(l -> renderRecipeLayer(recipe, mx, buffers, l, y));
        }
    }

    private void renderPreviewControls(PoseStack mx, AABB dims) {
        mx.pushPose();
        mx.translate(0, 0, 10);

        ResourceLocation sprites = new ResourceLocation(CompactCrafting.MOD_ID, "textures/gui/jei-sprites.png");

        if (exploded) {
            drawScaledTexture(mx, sprites, explodeToggle, 20, 0, 20, 20, 120, 20);
        } else {
            drawScaledTexture(mx, sprites, explodeToggle, 0, 0, 20, 20, 120, 20);
        }

        // Layer change buttons
        if (singleLayer) {
            drawScaledTexture(mx, sprites, layerSwap, 60, 0, 20, 20, 120, 20);
        } else {
            drawScaledTexture(mx, sprites, layerSwap, 40, 0, 20, 20, 120, 20);
        }

        if (singleLayer) {
            if (singleLayerOffset < dims.getYsize() - 1)
                drawScaledTexture(mx, sprites, layerUp, 80, 0, 20, 20, 120, 20);

            if (singleLayerOffset > 0) {
                drawScaledTexture(mx, sprites, layerDown, 100, 0, 20, 20, 120, 20);
            }
        }

        mx.popPose();
    }

    private void renderRecipeLayer(MiniaturizationRecipe recipe, PoseStack mx, MultiBufferSource.BufferSource buffers, IRecipeLayer l, int layerY) {
        // Begin layer
        mx.pushPose();

        AABB layerBounds = BlockSpaceUtil.getLayerBounds(recipe.getDimensions(), layerY);
        BlockPos.betweenClosedStream(layerBounds).forEach(filledPos -> {
            mx.pushPose();

            mx.translate(
                    ((filledPos.getX() + 0.5) * explodeMulti),
                    ((layerY + 0.5) * explodeMulti),
                    ((filledPos.getZ() + 0.5) * explodeMulti)
            );

            BlockPos zeroedPos = filledPos.below(layerY);
            Optional<String> componentForPosition = l.getComponentForPosition(zeroedPos);
            componentForPosition
                    .flatMap(recipe.getComponents()::getBlock)
                    .ifPresent(comp -> renderComponent(mx, buffers, comp, filledPos));

            mx.popPose();
        });

        // Done with layer
        mx.popPose();
    }

    private void renderComponent(PoseStack mx, MultiBufferSource.BufferSource buffers, IRecipeBlockComponent state, BlockPos filledPos) {
        // TODO - Render switching at fixed interval
        if (state.didErrorRendering())
            return;

        BlockState state1 = state.getRenderState();

        IModelData data = EmptyModelData.INSTANCE;
        if (previewLevel != null && state1.hasBlockEntity()) {
            // create fake world instance
            // get tile entity - extend EmptyBlockReader with impl
            BlockEntity be = previewLevel.getBlockEntity(filledPos);
            if (be != null)
                data = be.getModelData();
        }

        try {
//            final RenderBuffers buffs = Minecraft.getInstance().renderBuffers();
//            final BufferBuilder builder = buffs.fixedBufferPack().builder(RenderType.solid());
//            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
//            blocks.renderBatched(state1, BlockPos.ZERO, this.previewLevel, mx, builder, false, this.previewLevel.random, data);
//            builder.end();

            blocks.renderSingleBlock(state1,
                    mx,
                    buffers,
                    LightTexture.FULL_SKY,
                    OverlayTexture.NO_OVERLAY,
                    data);
        } catch (Exception e) {
            state.markRenderingErrored();

            CompactCrafting.LOGGER.warn("Error rendering block in preview: {}", state1.getBlock().getRegistryName());
            CompactCrafting.LOGGER.error("Stack Trace", e);
        }
    }

    public record ScreenArea(int x1, int y1, int x2, int y2) {

    }
}
 */