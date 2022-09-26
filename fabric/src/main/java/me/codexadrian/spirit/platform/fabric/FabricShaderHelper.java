package me.codexadrian.spirit.platform.fabric;

import me.codexadrian.spirit.fabric.FabricSoulShader;
import me.codexadrian.spirit.platform.fabric.services.IShaderHelper;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

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
