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
import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.NotNull;

public class SoulPedestalRenderer implements BlockEntityRenderer<SoulPedestalBlockEntity> {
    private final ItemRenderer itemRenderer;

    public SoulPedestalRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SoulPedestalBlockEntity blockEntity, float f, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource multiBufferSource, int i, int j) {
        if (!blockEntity.hasLevel() || blockEntity.isEmpty()) return;
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.85, 0.5D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(blockEntity.age % 360));
        matrixStack.translate(0, Math.sin(blockEntity.age * .1) * 0.05 + 0.05,0);
        itemRenderer.renderStatic(blockEntity.getItem(0), ItemTransforms.TransformType.GROUND, i, OverlayTexture.NO_OVERLAY, matrixStack, multiBufferSource, 0);
        matrixStack.popPose();
    }
}
