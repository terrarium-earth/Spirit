package me.codexadrian.spirit;

import me.codexadrian.spirit.client.SoulArrowEntityRenderer;
import me.codexadrian.spirit.client.SoulCageRenderer;
import me.codexadrian.spirit.platform.ClientServices;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SpiritClient {

    public static void initClient() {
        ClientServices.CLIENT.setRenderLayer(SpiritRegistry.BROKEN_SPAWNER.get(), RenderType.cutout());
        ClientServices.CLIENT.setRenderLayer(SpiritRegistry.SOUL_CAGE.get(), RenderType.cutout());
        ClientServices.CLIENT.registerBlockEntityRenderers(SpiritRegistry.SOUL_CAGE_ENTITY.get(), SoulCageRenderer::new);
        ClientServices.CLIENT.registerItemProperty(SpiritRegistry.SOUL_CRYSTAL.get(), new ResourceLocation(Constants.MODID, "activation"),
                (stack, level, entity, seed) -> stack.hasTag() ? SoulUtils.getActivation(stack) : 0);
        ClientServices.CLIENT.registerEntityRenderer(SpiritRegistry.SOUL_ARROW_ENTITY, SoulArrowEntityRenderer::new);
    }
}
