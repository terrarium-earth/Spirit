package me.codexadrian.spirit;

import me.codexadrian.spirit.platform.Services;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static me.codexadrian.spirit.Spirit.MODID;

public class Spirit {

    public static final String MODID = "spirit";
    public static final String MOD_NAME = "Spirit";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    public static final CreativeModeTab SPIRIT = Services.REGISTRY.registerCreativeTab(
            new ResourceLocation(MODID, "itemgroup"), () -> new ItemStack(SpiritRegistry.SOUL_CRYSTAL.get()));

    public static final int SOUL_COLOR = 0xFF00fffb;

    public static void onInitialize() {
        SpiritRegistry.registerAll();
    }
}
