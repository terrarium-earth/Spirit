package earth.terrarium.spirit.compat.rei.ingredients;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import earth.terrarium.spirit.compat.common.EntityIngredient;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.common.entry.EntryStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EntityIngredientRenderer implements EntryRenderer<EntityIngredient> {
    @Override
    public void render(EntryStack<EntityIngredient> entry, PoseStack matrices, Rectangle bounds, int mouseX, int mouseY, float delta) {
        Minecraft mc = Minecraft.getInstance();
        EntityIngredient value = entry.getValue();
        if (mc.level != null && value.getEntity() != null) {
            int y = 0;
            matrices.pushPose();
            matrices.translate(bounds.getX(), bounds.getY(), 0);
            renderEntity(matrices, value.getEntity(), mc.level, -2, y, value.getRotation(), 1);
            matrices.popPose();
        }
    }

    @Override
    @Nullable
    public Tooltip getTooltip(EntryStack<EntityIngredient> entry, TooltipContext context) {
        EntityIngredient value = entry.getValue();
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(value.getDisplayName());
        tooltip.addAll(value.getTooltip());
        return Tooltip.create(context.getPoint(), tooltip);
    }

    public static void renderEntity(PoseStack matrixStack, Entity entity, Level world, float x, float y, float rotation, float renderScale) {
        if (world == null) return;
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null) {
            var scaledSize = 12 / (Math.max(entity.getBbWidth(), entity.getBbHeight()));
            entity.tickCount = mc.player.tickCount;
            matrixStack.pushPose();
            matrixStack.translate(10, 20 * renderScale - 6, 0.5);
            matrixStack.translate(x, y, 1);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            matrixStack.translate(0, 0, 100);
            matrixStack.scale(-scaledSize * renderScale, scaledSize * renderScale, 30);
            matrixStack.mulPose(Axis.YP.rotationDegrees(rotation));
            EntityRenderDispatcher entityrenderermanager = mc.getEntityRenderDispatcher();
            MultiBufferSource.BufferSource renderTypeBuffer = mc.renderBuffers().bufferSource();
            entityrenderermanager.render(entity, 0, 0, 0.0D, mc.getFrameTime(), 1, matrixStack, renderTypeBuffer, 15728880);
            renderTypeBuffer.endBatch();
            matrixStack.popPose();
        }
    }
}
