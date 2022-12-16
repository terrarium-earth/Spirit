package earth.terrarium.spirit.common.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientUtils {
    public static boolean isItemInHand(ItemStack stack) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player != null) {
            return player.getMainHandItem().equals(stack) || player.getOffhandItem().equals(stack);
        }
        return false;
    }

    public static void shiftTooltip(List<Component> components, List<Component> onShiftComponents, List<Component> notShiftComponents) {
        Component shiftWord = Component.translatable("misc.spirit.shift.key").withStyle(Screen.hasShiftDown() ? ChatFormatting.WHITE : ChatFormatting.AQUA);
        MutableComponent shift = Component.translatable("misc.spirit.shift.shift_info", shiftWord).withStyle(ChatFormatting.GRAY);
        components.add(shift);
        if(Screen.hasShiftDown()) {
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
    public static void setSoulShader(ShaderInstance shader) {
        throw new NotImplementedException();
    }
}
