package me.codexadrian.spirit.client;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.entity.SoulEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SoulEntityRenderer extends LivingEntityRenderer<SoulEntity, SoulEntityModel> {

    public SoulEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new SoulEntityModel(), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull SoulEntity entity) {
        return new ResourceLocation(Spirit.MODID, "entity/soul_entity.png");
    }
}
