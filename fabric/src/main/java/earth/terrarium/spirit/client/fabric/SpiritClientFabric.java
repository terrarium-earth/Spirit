package earth.terrarium.spirit.client.fabric;

import earth.terrarium.spirit.client.SpiritClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import java.util.function.Supplier;

public class SpiritClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SpiritClient.init();
        registerRenderers();
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
