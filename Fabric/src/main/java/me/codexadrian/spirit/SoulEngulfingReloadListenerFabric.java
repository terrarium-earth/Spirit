package me.codexadrian.spirit;

import me.codexadrian.spirit.recipe.SoulEnglufingReloadListener;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;

public class SoulEngulfingReloadListenerFabric extends SoulEnglufingReloadListener implements IdentifiableResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(Constants.MODID, "soul_engulfing");
    }
}
