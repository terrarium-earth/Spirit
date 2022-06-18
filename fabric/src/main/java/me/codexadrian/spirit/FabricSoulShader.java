package me.codexadrian.spirit;

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


public class FabricSoulShader extends RenderType {

    public static ShaderInstance rendertypeTranslucentShader;

    public FabricSoulShader(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
    }

    public static <T extends Entity> RenderType getSoulRenderType(T entity, ResourceLocation texture) {
        return create(
                "mob_soul_layer_" + entity.getDisplayName().getString(),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true, true,
                RenderType.CompositeState
                        .builder()
                        .setShaderState(new RenderStateShard.ShaderStateShard(() -> rendertypeTranslucentShader))
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setCullState(NO_CULL)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                        .setLightmapState(LIGHTMAP)
                        .createCompositeState(true)
        );
    }
}
