package me.codexadrian.spirit;

import me.codexadrian.spirit.client.*;
import me.codexadrian.spirit.items.MobCrystalItem;
import me.codexadrian.spirit.platform.ClientServices;
import me.codexadrian.spirit.registry.SpiritBlocks;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.registry.SpiritMisc;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SpiritClient {

    public static void initClient() {
        ClientServices.CLIENT.setRenderLayer(SpiritBlocks.BROKEN_SPAWNER.get(), RenderType.cutout());
        ClientServices.CLIENT.setRenderLayer(SpiritBlocks.SOUL_CAGE.get(), RenderType.cutout());
        ClientServices.CLIENT.setRenderLayer(SpiritBlocks.SOUL_PEDESTAL.get(), RenderType.cutout());
        ClientServices.CLIENT.setRenderLayer(SpiritBlocks.PEDESTAL.get(), RenderType.cutout());
        ClientServices.CLIENT.setRenderLayer(SpiritBlocks.SOUL_GLASS.get(), RenderType.translucent());
        for (var glass : SpiritBlocks.SOUL_GLASS_BLOCKS) {
            ClientServices.CLIENT.setRenderLayer(glass.get(), RenderType.translucent());
        }
        ClientServices.CLIENT.registerBlockEntityRenderers(SpiritBlocks.SOUL_CAGE_ENTITY.get(), SoulCageRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderers(SpiritBlocks.SOUL_PEDESTAL_ENTITY.get(), SoulPedestalRenderer::new);
        ClientServices.CLIENT.registerBlockEntityRenderers(SpiritBlocks.PEDESTAL_ENTITY.get(), PedestalRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(SpiritMisc.SOUL_ARROW_ENTITY, SoulArrowEntityRenderer::new);
        ClientServices.CLIENT.registerEntityRenderer(SpiritMisc.SOUL_ENTITY, CrudeSoulEntityRenderer::new);
        ClientServices.CLIENT.registerItemProperty(SpiritItems.SOUL_BOW.get(), new ResourceLocation("pull"), (itemStack, clientLevel, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0f;
            }
            if (livingEntity.getUseItem() != itemStack) {
                return 0.0f;
            }
            return (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0f;
        });
        ClientServices.CLIENT.registerItemProperty(SpiritItems.SOUL_BOW.get(), new ResourceLocation("pulling"), (itemStack, clientLevel, livingEntity, i) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0f : 0.0f);
        ClientServices.CLIENT.registerItemProperty(SpiritItems.SOUL_CRYSTAL_SHARD.get(), new ResourceLocation(Spirit.MODID, "filled"), MobCrystalItem::mobCrystalType);
    }

}
