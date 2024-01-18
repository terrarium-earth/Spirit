package earth.terrarium.spirit.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import earth.terrarium.spirit.common.blockentity.SoulCageBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class SoulCageRenderer implements BlockEntityRenderer<SoulCageBlockEntity> {

    private final EntityRenderDispatcher entityRenderer;

    public SoulCageRenderer(BlockEntityRendererProvider.Context context) {
        entityRenderer = context.getEntityRenderer();
    }

    @Override
    public void render(SoulCageBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (!blockEntity.hasLevel() || blockEntity.getEntityId() == null) return;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.0D, 0.5D);
        var entity = blockEntity.getSpawner().getOrCreateDisplayEntity(blockEntity.getLevel());
        if (entity == null) return;
        float g = 0.53125F;
        float h = Math.max(entity.getBbWidth(), entity.getBbHeight());
        if ((double) h > 1.0D) {
            g /= h;
        }

        poseStack.translate(0.0D, 0.4000000059604645D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees((float) blockEntity.getSpawner().getSpin()));
        poseStack.translate(0.0D, -0.20000000298023224D, 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(-30.0F));
        poseStack.scale(g, g, g);
        entityRenderer.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTick, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
