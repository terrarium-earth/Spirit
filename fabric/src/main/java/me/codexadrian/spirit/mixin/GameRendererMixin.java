package me.codexadrian.spirit.mixin;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import me.codexadrian.spirit.platform.ClientServices;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Shadow
    @Final
    private Map<String, ShaderInstance> shaders;

    @Inject(method = "reloadShaders", at = @At("TAIL"))
    private void reloadShaders(ResourceManager resourceManager, CallbackInfo ci) {
        List<Pair<ShaderInstance, Consumer<ShaderInstance>>> list = new ArrayList<>();
        try {
            list.add(Pair.of(new ShaderInstance(resourceManager, "rendertype_entity_corrupted", DefaultVertexFormat.BLOCK), ClientServices.SHADERS::setSoulShader));
        } catch (Exception e) {
            list.forEach(pair -> pair.getFirst().close());
            throw new RuntimeException("could not reload shaders", e);
        }

        list.forEach(pair -> {
            ShaderInstance shaderInstance = pair.getFirst();
            this.shaders.put(shaderInstance.getName(), shaderInstance);
            pair.getSecond().accept(shaderInstance);
        });
    }
}
