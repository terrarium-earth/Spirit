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
        //TODO make soul bow shoot effects per entity type if the bow shoots from a soul crystal
        //Blaze shoot fire
        //Wither shoot with wither effect
        //Slime & Rabbit shoot with jump boost
        //Witch shoot with
        //Strays shoot with slowness
        //Husks shoot with hunger
        //Allays shoot with regeneration
        //Wardens shoot with Darkness
        //Cave Spiders & Bees shoot with poison
        //Cats shoot with Speed
        //Shulkers shoot levitation
        //Glowsquid shoots glowing
        //Dolphin shoots dolphin's grace
        SpiritRegistry.registerAll();
    }
}
