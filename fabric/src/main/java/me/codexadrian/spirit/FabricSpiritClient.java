package me.codexadrian.spirit;

import me.codexadrian.spirit.utils.SoulUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class FabricSpiritClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpiritClient.initClient();
    }
}
