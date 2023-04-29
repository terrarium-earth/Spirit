package earth.terrarium.spirit.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.spirit.api.souls.SoulfulCreature;
import earth.terrarium.spirit.common.util.ClientUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    private LivingEntity currentlyRendered;

    @Inject(method = "render", at = @At("HEAD"))
    private void preRender(LivingEntity livingEntity, float f, float g, PoseStack poseStack,
                           MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (((SoulfulCreature) livingEntity).isSoulless()) {
            currentlyRendered = livingEntity;
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void postRender(LivingEntity livingEntity, float f, float g, PoseStack poseStack,
                            MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (currentlyRendered != null) {
            currentlyRendered = null;
        }
    }

    @Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
    private void getRenderType(LivingEntity livingEntity, boolean bl, boolean bl2, boolean bl3,
                               CallbackInfoReturnable<RenderType> cir) {
        if (((SoulfulCreature) livingEntity).isSoulless()) {
            cir.setReturnValue(ClientUtils.getSoulShader(livingEntity, ((LivingEntityRenderer) (Object) this).getTextureLocation(livingEntity)));
        }
    }
}
