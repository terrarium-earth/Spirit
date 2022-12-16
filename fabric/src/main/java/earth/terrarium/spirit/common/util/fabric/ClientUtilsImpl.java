package earth.terrarium.spirit.common.util.fabric;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ClientUtilsImpl {

    public static <T extends Entity> RenderType getSoulShader(T entity, ResourceLocation texture) {
        return FabricSoulShader.getSoulRenderType(entity, texture);
    }

    public static void setSoulShader(ShaderInstance shader) {
        FabricSoulShader.rendertypeTranslucentShader = shader;
    }
}
