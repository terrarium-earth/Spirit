package me.codexadrian.spirit.fabric;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.entity.CrudeSoulEntity;
import me.codexadrian.spirit.fabric.SpiritConfigImpl;
import me.codexadrian.spirit.platform.Services;
import me.codexadrian.spirit.registry.SpiritMisc;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class FabricSpirit implements ModInitializer {

    @Override
    public void onInitialize() {
        Spirit.onInitialize();
        SpiritConfigImpl.loadConfig(Services.PLATFORM.getConfigDir());
        FabricDefaultAttributeRegistry.register(SpiritMisc.SOUL_ENTITY.get(), CrudeSoulEntity.createMobAttributes());
    }
}
