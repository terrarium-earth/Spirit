package me.codexadrian.spirit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class FabricSpirit implements ModInitializer {
    public static final SoulEngulfingReloadListenerFabric INSTANCE = new SoulEngulfingReloadListenerFabric();

    @Override
    public void onInitialize() {
        Spirit.onInitialize();
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(INSTANCE);
    }
}
