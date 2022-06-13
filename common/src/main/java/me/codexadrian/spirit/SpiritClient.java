package me.codexadrian.spirit;

import me.codexadrian.spirit.client.PedestalRenderer;
import me.codexadrian.spirit.client.SoulArrowEntityRenderer;
import me.codexadrian.spirit.client.SoulCageRenderer;
import me.codexadrian.spirit.client.SoulPedestalRenderer;
import me.codexadrian.spirit.platform.ClientServices;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class SpiritClient {

    public static void initClient() {
        ClientServices.CLIENT.setRenderLayer(SpiritRegistry.BROKEN_SPAWNER.get(), RenderType.cutout());
        ClientServices.CLIENT.setRenderLayer(SpiritRegistry.SOUL_CAGE.get(), RenderType.cutout());
        ClientServices.CLIENT.setRenderLayer(SpiritRegistry.SOUL_PEDESTAL.get(), RenderType.cutout());
        ClientServices.CLIENT.setRenderLayer(SpiritRegistry.PEDESTAL.get(), RenderType.cutout());
        ClientServices.CLIENT.setRenderLayer(SpiritRegistry.SOUL_GLASS.get(), RenderType.translucent());
        for (var glass : SpiritRegistry.SOUL_GLASS_BLOCKS) {
            ClientServices.CLIENT.setRenderLayer(glass.get(), RenderType.translucent());
        }
        ClientServices.CLIENT.registerBlockEntityRenderers(SpiritRegistry.SOUL_CAGE_ENTITY.get(), SoulCageRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderers(SpiritRegistry.SOUL_PEDESTAL_ENTITY.get(), SoulPedestalRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderers(SpiritRegistry.PEDESTAL_ENTITY.get(), PedestalRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(SpiritRegistry.SOUL_ARROW_ENTITY, SoulArrowEntityRenderer::new);
        ClientServices.CLIENT.registerItemProperty(SpiritRegistry.SOUL_BOW.get(), new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0f;
            }
            if (livingEntity.getUseItem() != itemStack) {
                return 0.0f;
            }
            return (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0f;
        });
        ClientServices.CLIENT.registerItemProperty(SpiritRegistry.SOUL_BOW.get(), new ResourceLocation("pulling"), (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f);
    }
}
