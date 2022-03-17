package me.codexadrian.spirit;

import me.codexadrian.spirit.blocks.soulcage.SoulCageRenderer;
import me.codexadrian.spirit.platform.Services;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SpiritClient {
    public static void initClient() {
        Services.CLIENT.setRenderLayer(SpiritRegistry.BROKEN_SPAWNER.get(), RenderType.cutout());
        Services.CLIENT.setRenderLayer(SpiritRegistry.SOUL_CAGE.get(), RenderType.cutout());
        Services.CLIENT.registerEntityRenderer(SpiritRegistry.SOUL_CAGE_ENTITY.get(), SoulCageRenderer::new);
        Services.CLIENT.registerItemProperty(SpiritRegistry.SOUL_CRYSTAL.get(), new ResourceLocation(Constants.MODID, "activation"), (stack, level, entity, seed) -> stack.hasTag() ? SoulUtils.getActivation(stack) : 0);
    }
}
