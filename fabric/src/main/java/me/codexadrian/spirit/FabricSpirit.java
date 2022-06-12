package me.codexadrian.spirit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class FabricSpirit implements ModInitializer {

    @Override
    public void onInitialize() {
        Spirit.onInitialize();
    }
}
