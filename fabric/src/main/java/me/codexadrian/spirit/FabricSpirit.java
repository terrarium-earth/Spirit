package me.codexadrian.spirit;

import me.codexadrian.spirit.entity.SoulEntity;
import me.codexadrian.spirit.fabric.SpiritConfigImpl;
import me.codexadrian.spirit.platform.Services;
import me.codexadrian.spirit.registry.SpiritMisc;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

public class FabricSpirit implements ModInitializer {

    @Override
    public void onInitialize() {
        Spirit.onInitialize();
        SpiritConfigImpl.loadConfig(Services.PLATFORM.getConfigDir());
        FabricDefaultAttributeRegistry.register(SpiritMisc.SOUL_ENTITY.get(), SoulEntity.createMobAttributes());
    }
}
