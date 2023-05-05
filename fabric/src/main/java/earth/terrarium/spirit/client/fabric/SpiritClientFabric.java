package earth.terrarium.spirit.client.fabric;

import earth.terrarium.spirit.client.SpiritClient;
import earth.terrarium.spirit.client.entity.SoulReceptacleRenderer;
import earth.terrarium.spirit.common.registry.SpiritEntities;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public class SpiritClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SpiritClient.init();
        registerRenderers();
        ColorProviderRegistry.ITEM.register(SpiritClient.ARMOR_COLOR, SpiritItems.SOUL_STEEL_HELMET.get(), SpiritItems.SOUL_STEEL_CHESTPLATE.get(), SpiritItems.SOUL_STEEL_LEGGINGS.get(), SpiritItems.SOUL_STEEL_BOOTS.get());
        ColorProviderRegistry.ITEM.register(SpiritClient.TOOL_COLOR, SpiritItems.SOUL_STEEL_HAMMER.get(), SpiritItems.SOUL_STEEL_EXCAVATOR.get(), SpiritItems.SOUL_STEEL_BATTLEAXE.get(), SpiritItems.SCYTHE.get());
        EntityRendererRegistry.register(SpiritEntities.SOUL_RECEPTACLE.get(), SoulReceptacleRenderer::new);
    }

    private static void registerRenderers() {
        SpiritClient.registerEntityLayers(new SpiritClient.LayerDefinitionRegistry() {
            @Override
            public void register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
                EntityModelLayerRegistry.registerModelLayer(location, definition::get);
            }
        });
    }
}
