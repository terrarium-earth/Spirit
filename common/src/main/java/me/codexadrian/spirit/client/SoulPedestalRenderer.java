package me.codexadrian.spirit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import me.codexadrian.spirit.blocks.blockentity.SoulPedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.NotNull;

public class SoulPedestalRenderer implements BlockEntityRenderer<SoulPedestalBlockEntity> {
    private final ItemRenderer itemRenderer;

    public SoulPedestalRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SoulPedestalBlockEntity blockEntity, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource multiBufferSource, int i, int j) {
        if (!blockEntity.hasLevel()) return;
        if(blockEntity.type != null) {
            var entity = blockEntity.getOrCreateEntity();
            entity.tickCount = blockEntity.age;
            entity.tick();
            float g = 0.75F;
            float h = Math.max(entity.getBbWidth(), entity.getBbHeight());
            if ((double) h > 1.0D) {
                g /= h;
            }

            matrixStack.pushPose();
            matrixStack.translate(0.5D, .75D, 0.5D);
            var degrees = blockEntity.age;
            var oldTick = Math.max(blockEntity.age - 1, 0);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, oldTick, degrees) % 360));
            matrixStack.scale(g, g, g);
            matrixStack.translate(0, Math.sin(blockEntity.age * .1) * 0.05 + 0.05,0);
            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStack, multiBufferSource, i);
            matrixStack.popPose();
        }
    }
}
