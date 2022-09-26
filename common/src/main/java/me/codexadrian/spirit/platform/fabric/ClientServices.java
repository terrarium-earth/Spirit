package me.codexadrian.spirit.platform.fabric;

import me.codexadrian.spirit.platform.fabric.services.IClientHelper;
import me.codexadrian.spirit.platform.fabric.services.IShaderHelper;

public class ClientServices {

    public static final IClientHelper CLIENT = Services.load(IClientHelper.class);
    public static final IShaderHelper SHADERS = Services.load(IShaderHelper.class);
}
