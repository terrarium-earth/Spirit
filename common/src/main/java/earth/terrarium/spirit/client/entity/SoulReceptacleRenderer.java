package earth.terrarium.spirit.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.entity.SoulReceptacle;
import earth.terrarium.spirit.common.util.ClientUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SoulReceptacleRenderer extends EntityRenderer<SoulReceptacle> {
    private final SoulReceptacleModel model;
    public SoulReceptacleRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new SoulReceptacleModel(context.bakeLayer(SoulReceptacleModel.LAYER_LOCATION));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SoulReceptacle entity) {
        return new ResourceLocation(Spirit.MODID, "textures/entity/soul_receptacle.png");
    }

    @Override
    public void render(SoulReceptacle entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(entity, f, g, poseStack, multiBufferSource, i);
        poseStack.pushPose();
        poseStack.translate(0, -0.75, 0);
        poseStack.translate(0, Math.cos(entity.tickCount / 10.0) / 4, 0);
        model.renderToBuffer(poseStack, multiBufferSource.getBuffer(ClientUtils.getSoulShader(entity, this.getTextureLocation(entity))), i, getOverlayCoords(0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    public static int getOverlayCoords(float f) {
        return OverlayTexture.pack(OverlayTexture.u(f), OverlayTexture.v(false));
    }
}
