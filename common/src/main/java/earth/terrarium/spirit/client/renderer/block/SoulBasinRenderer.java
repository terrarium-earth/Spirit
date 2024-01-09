package earth.terrarium.spirit.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import earth.terrarium.botarium.common.fluid.utils.FluidHooks;
import earth.terrarium.spirit.client.renderer.utils.FluidHolderRenderer;
import earth.terrarium.spirit.common.blockentity.SoulBasinBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class SoulBasinRenderer implements BlockEntityRenderer<SoulBasinBlockEntity> {
    private final ItemRenderer itemRenderer;

    public SoulBasinRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(SoulBasinBlockEntity blockEntity, float partialTicks, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource multiBufferSource, int i, int j) {
        if (!blockEntity.hasLevel()) return;
        if (!blockEntity.getFluidContainer().isEmpty()) {
            FluidHolderRenderer.renderFluid(blockEntity.getFluidContainer().getFluids().get(0), FluidHooks.buckets(4), matrixStack, multiBufferSource, i);
        }
    }
}
