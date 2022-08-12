package me.codexadrian.spirit.platform;

import me.codexadrian.spirit.FabricSoulShader;
import me.codexadrian.spirit.platform.services.IShaderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class FabricShaderHelper implements IShaderHelper {

    @Override
    public void setSoulShader(ShaderInstance shader) {
        FabricSoulShader.rendertypeTranslucentShader = shader;
    }

    @Override
    public <T extends Entity> RenderType getSoulShader(T entity, ResourceLocation texture) {
        return FabricSoulShader.getSoulRenderType(entity, texture);
    }
}
