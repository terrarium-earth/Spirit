package me.codexadrian.spirit;

import me.codexadrian.spirit.fabric.SpiritConfigImpl;
import me.codexadrian.spirit.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

public class FabricSpirit implements ModInitializer {

    @Override
    public void onInitialize() {
        Spirit.onInitialize();
        SpiritConfigImpl.loadConfig(Services.PLATFORM.getConfigDir());
    }
}
