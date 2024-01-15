package earth.terrarium.spirit.client.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.client.SpiritClientApi;
import earth.terrarium.spirit.api.rituals.results.RitualResult;
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
    private final EntityRendererProvider.Context context;
    public SoulReceptacleRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(SoulReceptacle entity) {
        return new ResourceLocation(Spirit.MODID, "textures/entity/soul_receptacle.png");
    }

    @Override
    public void render(SoulReceptacle entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        RitualResult<?> recipeResult = entity.getRecipeResult();
        if (recipeResult != null) {
            var renderer = SpiritClientApi.getReceptacleRenderer(recipeResult.getClass());
            if (renderer != null) {
                renderer.render(context, entity, recipeResult, partialTick, poseStack, buffer, packedLight);
            }
        }
    }

    public static int getOverlayCoords(float f) {
        return OverlayTexture.pack(OverlayTexture.u(f), OverlayTexture.v(false));
    }
}
