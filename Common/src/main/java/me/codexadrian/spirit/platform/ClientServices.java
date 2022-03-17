package me.codexadrian.spirit.platform;

import me.codexadrian.spirit.platform.services.IClientHelper;
import me.codexadrian.spirit.platform.services.IShaderHelper;

public class ClientServices {

    public static final IClientHelper CLIENT = Services.load(IClientHelper.class);
    public static final IShaderHelper SHADERS = Services.load(IShaderHelper.class);
}
