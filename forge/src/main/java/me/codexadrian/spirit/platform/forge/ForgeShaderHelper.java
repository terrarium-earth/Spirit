package me.codexadrian.spirit.platform.forge;

import me.codexadrian.spirit.forge.ForgeSoulShader;
import me.codexadrian.spirit.platform.services.IShaderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ForgeShaderHelper implements IShaderHelper {

    @Override
    public void setSoulShader(ShaderInstance shader) {
        ForgeSoulShader.rendertypeTranslucentShader = shader;
    }

    @Override
    public <T extends Entity> RenderType getSoulShader(T entity, ResourceLocation texture) {
        return ForgeSoulShader.getSoulRenderType(entity, texture);
    }
}
