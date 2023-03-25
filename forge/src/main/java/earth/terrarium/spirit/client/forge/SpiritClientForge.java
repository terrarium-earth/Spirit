package earth.terrarium.spirit.client.forge;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.client.SpiritClient;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Spirit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpiritClientForge {

    public static void init() {
        SpiritClient.init();
    }

    @SubscribeEvent
    public void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        SpiritClient.registerEntityLayers(new SpiritClient.LayerDefinitionRegistry() {
            @Override
            public void register(ModelLayerLocation location, Supplier<LayerDefinition> definition) {
                event.registerLayerDefinition(location, definition);
            }
        });
    }

    @SubscribeEvent
    public static void registerColor(RegisterColorHandlersEvent.Item event) {
        event.register(SpiritClient.ARMOR_COLOR, SpiritItems.SOUL_STEEL_HELMET.get(), SpiritItems.SOUL_STEEL_CHESTPLATE.get(), SpiritItems.SOUL_STEEL_LEGGINGS.get(), SpiritItems.SOUL_STEEL_BOOTS.get());
    }
}
