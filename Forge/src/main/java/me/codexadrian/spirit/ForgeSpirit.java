package me.codexadrian.spirit;

import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeSpirit {

    public ForgeSpirit() {
        Spirit.onInitialize();
    }
}