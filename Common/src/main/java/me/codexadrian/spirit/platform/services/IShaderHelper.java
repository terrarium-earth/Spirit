package me.codexadrian.spirit.platform.services;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

public interface IShaderHelper {

    void setSoulShader(ShaderInstance shader);

    RenderType getSoulShader(LivingEntity entity, LivingEntityRenderer livingEntity);
}
