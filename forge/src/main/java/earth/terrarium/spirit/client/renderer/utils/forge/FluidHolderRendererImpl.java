package earth.terrarium.spirit.client.renderer.utils.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class FluidHolderRendererImpl {
    public static TextureAtlasSprite getWaterSprite() {
        IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(Fluids.WATER);
        ResourceLocation resourceLocation = extension.getStillTexture(new FluidStack(Fluids.WATER, 1000));
        return Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(resourceLocation);
    }
}
