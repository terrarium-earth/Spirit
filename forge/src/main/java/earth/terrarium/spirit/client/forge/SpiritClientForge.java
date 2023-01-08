package earth.terrarium.spirit.client.forge;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.client.SpiritClient;
import earth.terrarium.spirit.common.handlers.MobCrystalHandler;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Spirit.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SpiritClientForge {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        SpiritClient.init();
    }
}
