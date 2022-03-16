package me.codexadrian.spirit;

import me.codexadrian.spirit.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;

import static me.codexadrian.spirit.Constants.MODID;

public class Spirit {

    public static final CreativeModeTab SPIRIT = Services.REGISTRY.registerCreativeTab(new ResourceLocation(MODID, "itemgroup"), () -> new ItemStack(SpiritRegistry.SOUL_CRYSTAL.get()));

    private static SpiritConfig spiritConfig;

    public static SpiritConfig getSpiritConfig() {
        return spiritConfig;
    }

    public static void onInitialize() {
        try {
            spiritConfig = SpiritConfig.loadConfig(Services.PLATFORM.getConfigDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
