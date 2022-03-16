package me.codexadrian.spirit.platform;

import me.codexadrian.spirit.FabricSoulShader;
import me.codexadrian.spirit.platform.services.IShaderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

public class FabricShaderHelper implements IShaderHelper {
    @Override
    public void setSoulShader(ShaderInstance shader) {
        FabricSoulShader.rendertypeTranslucentShader = shader;
    }

    @Override
    public RenderType getSoulShader(LivingEntity entity, LivingEntityRenderer livingEntity) {
        return FabricSoulShader.getSoulRenderType(entity, livingEntity);
    }
}
