package me.codexadrian.spirit;

import me.codexadrian.spirit.platform.ForgeRegistryHelper;
import me.codexadrian.spirit.recipe.SoulEnglufingReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MODID)
public class ForgeSpirit {

    public ForgeSpirit() {
        Spirit.onInitialize();

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeRegistryHelper.BLOCKS.register(eventBus);
        ForgeRegistryHelper.ITEMS.register(eventBus);
        ForgeRegistryHelper.BLOCK_ENTITIES.register(eventBus);
        eventBus.addListener(this::imcEvent);
        MinecraftForge.EVENT_BUS.addListener(this::reloadListenerEvent);
    }

    private void imcEvent(InterModEnqueueEvent event) {
        if(ModList.get().isLoaded("theoneprobe")) {
            //InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPCompat::new);
        }
    }

    private void reloadListenerEvent(AddReloadListenerEvent event) {
        event.addListener(new SoulEnglufingReloadListener());
    }
}