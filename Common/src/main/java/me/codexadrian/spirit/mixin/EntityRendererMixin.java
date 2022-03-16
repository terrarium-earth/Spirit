package me.codexadrian.spirit.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.codexadrian.spirit.Corrupted;
import me.codexadrian.spirit.platform.Services;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class EntityRendererMixin {

    private LivingEntity currentlyRendered;

    @Inject(method = "render", at = @At("HEAD"))
    private void preRender(LivingEntity livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (((Corrupted) livingEntity).isCorrupted()) {
            currentlyRendered = livingEntity;
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void postRender(LivingEntity livingEntity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        if (currentlyRendered != null) {
            currentlyRendered = null;
        }
    }

    @Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
    private void getRenderType(LivingEntity livingEntity, boolean bl, boolean bl2, boolean bl3, CallbackInfoReturnable<RenderType> cir) {
        if (((Corrupted) livingEntity).isCorrupted()) {
            cir.setReturnValue(Services.SHADERS.getSoulShader(livingEntity, (LivingEntityRenderer) (Object) this));
        }
    }
}

