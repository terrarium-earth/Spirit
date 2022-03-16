package me.codexadrian.spirit;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;


public class ForgeSoulShader extends RenderType {

    public static ShaderInstance rendertypeTranslucentShader;

    public ForgeSoulShader(String s, VertexFormat v, VertexFormat.Mode m, int i, boolean b, boolean b2, Runnable r, Runnable r2) {
        super(s, v, m, i, b, b2, r, r2);
    }

    public static RenderType getSoulRenderType(LivingEntity entity, LivingEntityRenderer livingEntity) {
        return RenderType.create(
                "mob_soul_layer_" + entity.getDisplayName().getString(),
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true, true,
                CompositeState
                        .builder()
                        .setShaderState(new ShaderStateShard(() -> rendertypeTranslucentShader))
                        .setTextureState(new TextureStateShard(livingEntity.getTextureLocation(entity), false, false))
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .createCompositeState(true)
        );
    }
}
