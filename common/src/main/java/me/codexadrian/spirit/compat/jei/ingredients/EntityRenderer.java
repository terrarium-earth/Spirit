package me.codexadrian.spirit.compat.jei.ingredients;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 * @author Team Resourceful
 */
public class EntityRenderer implements IIngredientRenderer<EntityIngredient> {

    @Override
    public void render(@NotNull PoseStack stack, @NotNull EntityIngredient ingredient) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && ingredient.getEntity() != null) {
            int y = 0;
            renderEntity(stack, ingredient.getEntity(), ingredient.getSoulCount(), mc.level, -2, y, ingredient.getRotation(), 1);
        }
    }

    @NotNull
    @Override
    public List<Component> getTooltip(EntityIngredient entityIngredient, @NotNull TooltipFlag iTooltipFlag) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(entityIngredient.getDisplayName());
        tooltip.addAll(entityIngredient.getTooltip());
        return tooltip;
    }

    public static void renderEntity(PoseStack matrixStack, Entity entity, int count, Level world, float x, float y, float rotation, float renderScale) {
        if (world == null) return;
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null) {
            var scaledSize = 20 / (Math.max(entity.getBbWidth(), entity.getBbHeight()));
            entity.tickCount = mc.player.tickCount;
            matrixStack.pushPose();
            matrixStack.translate(14, 20 * renderScale + 4, 0.5);
            matrixStack.translate(x, y, 1);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
            matrixStack.translate(0, 0, 100);
            matrixStack.scale(- scaledSize * renderScale, scaledSize * renderScale, 30);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
            EntityRenderDispatcher entityrenderermanager = mc.getEntityRenderDispatcher();
            MultiBufferSource.BufferSource renderTypeBuffer = mc.renderBuffers().bufferSource();
            entityrenderermanager.render(entity, 0, 0, 0.0D, mc.getFrameTime(), 1, matrixStack, renderTypeBuffer, 15728880);
            renderTypeBuffer.endBatch();
            matrixStack.popPose();

            if (count != 1) {
                Font font = Minecraft.getInstance().font;
                String string2 = String.valueOf(count);
                matrixStack.translate(7.0, 7.0, 200.0F);
                MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                font.drawInBatch(string2, x + 17 - font.width(string2), y + 9, 16777215, true, matrixStack.last().pose(), bufferSource, false, 0, 15728880);
                bufferSource.endBatch();
            }
        }
    }

}