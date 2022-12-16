package earth.terrarium.spirit.forge;

import earth.terrarium.spirit.Spirit;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Spirit.MODID)
public class SpiritForge {
    public SpiritForge() {
        Spirit.init();
    }
}