package me.codexadrian.spirit.client;

import me.codexadrian.spirit.Corrupted;
import me.codexadrian.spirit.platform.fabric.ClientServices;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class SpiritCompatHelper {
    public static RenderType getRenderType(Entity livingEntity, ResourceLocation texture, RenderType renderType) {
        if(livingEntity instanceof Corrupted corrupted && corrupted.isCorrupted()) {
            return ClientServices.SHADERS.getSoulShader(livingEntity, texture);
        }
        return renderType;
    }
}
