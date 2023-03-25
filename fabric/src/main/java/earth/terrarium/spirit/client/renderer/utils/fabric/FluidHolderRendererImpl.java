package earth.terrarium.spirit.client.renderer.utils.fabric;

import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.Fluids;

public class FluidHolderRendererImpl {
    public static TextureAtlasSprite getWaterSprite() {
        return FluidVariantRendering.getSprite(FluidVariant.of(Fluids.WATER));
    }
}
