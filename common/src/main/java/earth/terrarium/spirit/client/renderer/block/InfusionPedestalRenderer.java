package earth.terrarium.spirit.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import earth.terrarium.spirit.common.blockentity.InfusionTableBlockEntity;
import earth.terrarium.spirit.common.blockentity.PedestalBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import org.jetbrains.annotations.NotNull;

public class InfusionPedestalRenderer implements BlockEntityRenderer<InfusionTableBlockEntity> {
    private final ItemRenderer itemRenderer;

    public InfusionPedestalRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(InfusionTableBlockEntity blockEntity, float f, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource multiBufferSource, int i, int j) {
        if (!blockEntity.hasLevel() || blockEntity.isEmpty()) return;
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 1.05D, 0.5D);
        matrixStack.mulPose(Axis.YP.rotationDegrees(blockEntity.age % 360));
        matrixStack.scale(0.55f, 0.55f, 0.55f);
        matrixStack.translate(0, Math.sin(blockEntity.age * .1) * 0.05 + 0.05, 0);
        itemRenderer.renderStatic(blockEntity.getItem(0), ItemDisplayContext.NONE, i, OverlayTexture.NO_OVERLAY, matrixStack, multiBufferSource, blockEntity.getLevel(), 0);
        matrixStack.popPose();
    }
}
