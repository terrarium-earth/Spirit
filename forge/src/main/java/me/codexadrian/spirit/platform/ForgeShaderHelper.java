package me.codexadrian.spirit.platform;

import me.codexadrian.spirit.ForgeSoulShader;
import me.codexadrian.spirit.platform.services.IShaderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ForgeShaderHelper implements IShaderHelper {

    @Override
    public void setSoulShader(ShaderInstance shader) {
        ForgeSoulShader.rendertypeTranslucentShader = shader;
    }

    @Override
    public <T extends Entity> RenderType getSoulShader(T entity, EntityRenderer<T> livingEntity) {
        return ForgeSoulShader.getSoulRenderType(entity, livingEntity);
    }
}
