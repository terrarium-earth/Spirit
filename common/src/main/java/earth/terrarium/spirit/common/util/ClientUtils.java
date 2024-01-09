package earth.terrarium.spirit.common.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.common.network.NetworkHandling;
import earth.terrarium.spirit.common.network.messages.JumpKeyPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.client.renderer.RenderStateShard.*;

@Environment(EnvType.CLIENT)
public class ClientUtils {
    public static boolean clickingJump;
    private static boolean sentJumpPacket;

    public static boolean isItemInHand(ItemStack stack) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            return player.getMainHandItem().equals(stack) || player.getOffhandItem().equals(stack);
        }
        return false;
    }

    public static void shiftTooltip(List<Component> components, List<? extends Component> onShiftComponents, List<? extends Component> notShiftComponents) {
        Component shiftWord = Component.translatable("misc.spirit.shift.key").withStyle(Screen.hasShiftDown() ? ChatFormatting.WHITE : ChatFormatting.AQUA);
        MutableComponent shift = Component.translatable("misc.spirit.shift.shift_info", shiftWord).withStyle(ChatFormatting.GRAY);
        components.add(shift);
        if (Screen.hasShiftDown()) {
            components.addAll(onShiftComponents);
        } else {
            components.addAll(notShiftComponents);
        }
    }

    public static void shiftTooltip(List<Component> components, List<Component> onShiftComponents) {
        shiftTooltip(components, onShiftComponents, List.of());
    }

    @ExpectPlatform
    public static <T extends Entity> RenderType getSoulShader(T entity, ResourceLocation texture) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static RenderType getBasinShader() {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static void setSoulShader(ShaderInstance shader) {
        throw new NotImplementedException();
    }

    private static final Function<ResourceLocation, RenderType> BEACON_BEAM = Util.memoize((resourceLocation) -> {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(CULL).setWriteMaskState(COLOR_WRITE).setOverlayState(OVERLAY).createCompositeState(true);
        return RenderType.create("entity_translucent_emissive", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, compositeState);
    });

    public static RenderType armorRenderType(ResourceLocation resourceLocation) {
        return BEACON_BEAM.apply(resourceLocation);
    }

    public static void onStartTick(Minecraft minecraft) {
        if (minecraft.level != null) {
            clickingJump = minecraft.options.keyJump.isDown();

            if (clickingJump && sentJumpPacket) {
                NetworkHandling.CHANNEL.sendToServer(new JumpKeyPacket(true));
                sentJumpPacket = false;
            }

            if (!(clickingJump || sentJumpPacket)) {
                NetworkHandling.CHANNEL.sendToServer(new JumpKeyPacket(false));
                sentJumpPacket = true;
            }
        }
    }

    public static void init() {
    }
}
