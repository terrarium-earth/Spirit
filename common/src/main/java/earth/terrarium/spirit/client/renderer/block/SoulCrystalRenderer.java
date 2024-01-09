package earth.terrarium.spirit.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import earth.terrarium.spirit.client.renderer.utils.FluidHolderRenderer;
import earth.terrarium.spirit.common.blockentity.SoulBasinBlockEntity;
import earth.terrarium.spirit.common.blockentity.SoulCrystalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class SoulCrystalRenderer implements BlockEntityRenderer<SoulCrystalBlockEntity> {
    private final ItemRenderer itemRenderer;

    public SoulCrystalRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SoulCrystalBlockEntity blockEntity, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource multiBufferSource, int i, int j) {
        if (!blockEntity.hasLevel()) return;
        if (!blockEntity.getContainer().isEmpty()) {
            var entity = blockEntity.getOrCreateEntity();
            entity.tickCount = blockEntity.age;
            entity.tick();
            float g = 0.5F;
            if ((double) entity.getBbWidth() > 0.75D) {
                g /= entity.getBbWidth();
            }

            matrixStack.pushPose();
            matrixStack.translate(0.5D, 0.9375, 0.5D);
            var degrees = blockEntity.age;
            var oldTick = Math.max(blockEntity.age - 1, 0);
            matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, oldTick, degrees) % 360));
            matrixStack.scale(g, g, g);
            matrixStack.translate(0, Math.sin(blockEntity.age * .1) * 0.05 + 0.05, 0);
            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStack, multiBufferSource, i);
            matrixStack.popPose();
        }
    }
}
