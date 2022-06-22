package me.codexadrian.spirit.client;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.entity.CrudeSoulEntity;
import me.codexadrian.spirit.platform.ClientServices;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrudeSoulEntityRenderer extends LivingEntityRenderer<CrudeSoulEntity, CrudeSoulEntityModel> {

    public CrudeSoulEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CrudeSoulEntityModel(context.bakeLayer(CrudeSoulEntityModel.LAYER_LOCATION)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull CrudeSoulEntity entity) {
        return new ResourceLocation(Spirit.MODID, "textures/entity/crude_soul.png");
    }

    @Nullable
    @Override
    protected RenderType getRenderType(@NotNull CrudeSoulEntity livingEntity, boolean bl, boolean bl2, boolean bl3) {
        return ClientServices.SHADERS.getSoulShader(livingEntity, this.getTextureLocation(livingEntity));
    }
}
