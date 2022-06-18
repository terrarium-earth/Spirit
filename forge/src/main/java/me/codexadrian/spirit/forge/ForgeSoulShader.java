package me.codexadrian.spirit.forge;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ForgeSoulShader extends RenderType {

    public static ShaderInstance rendertypeTranslucentShader;

    public ForgeSoulShader(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2) {
        super(s, v, m, i, b, b2, r, r2);
    }

    public static <T extends Entity> RenderType getSoulRenderType(T entity, ResourceLocation texture) {
        return RenderType.create(
                "mob_soul_layer_" + entity.getDisplayName().getString(),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true, true,
                CompositeState
                        .builder()
                        .setShaderState(new ShaderStateShard(() -> rendertypeTranslucentShader))
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                        .setLightmapState(LIGHTMAP)
                        .createCompositeState(true)
        );
    }
}
