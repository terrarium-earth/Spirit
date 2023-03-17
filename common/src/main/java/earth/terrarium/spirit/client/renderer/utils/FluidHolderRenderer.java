package earth.terrarium.spirit.client.renderer.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.util.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class FluidHolderRenderer {
    public static TextureAtlasSprite SPRITE = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(new ResourceLocation(Spirit.MODID, "block/soul_glass.png"));

    public static void renderFluid(int amount, int maxAmount, PoseStack stack, MultiBufferSource source, int light) {
        float percent = (float) amount / (float) maxAmount;
        if (percent > 0) {
            stack.pushPose();
            int color = Spirit.SOUL_COLOR;
            createQuad(stack, source.getBuffer(ClientUtils.getBasinShader()), Mth.lerp(percent, (float) bounds().minY, (float) bounds().maxY), light, color, SPRITE);
            stack.popPose();
        }
    }

    private static void createQuad(PoseStack stack, VertexConsumer builder, float verticalOffset, int light, int color, TextureAtlasSprite texture) {
        Matrix4f matrix4f = stack.last().pose();
        float x1 = (float) bounds().minX;
        float x2 = (float) bounds().maxX;
        float z1 = (float) bounds().minZ;
        float z2 = (float) bounds().maxZ;
        int r = FastColor.ARGB32.red(color);
        int g = FastColor.ARGB32.green(color);
        int b = FastColor.ARGB32.blue(color);
        int a = FastColor.ARGB32.alpha(color);
        builder.vertex(matrix4f, x1, verticalOffset, z2).color(r, g, b, a).uv(texture.getU(0), texture.getV(0)).uv2(light).normal(0, 0F, 0).endVertex();
        builder.vertex(matrix4f, x2, verticalOffset, z2).color(r, g, b, a).uv(texture.getU(64), texture.getV(0)).uv2(light).normal(0, 0F, 0).endVertex();
        builder.vertex(matrix4f, x2, verticalOffset, z1).color(r, g, b, a).uv(texture.getU(64), texture.getV(64)).uv2(light).normal(0, 0F, 0).endVertex();
        builder.vertex(matrix4f, x1, verticalOffset, z1).color(r, g, b, a).uv(texture.getU(0), texture.getV(64)).uv2(light).normal(0, 0F, 0).endVertex();
    }

    public static @NotNull AABB bounds() {
        return new AABB(0.25, 0.6875, 0.25, 0.75, 0.875, 0.75);
    }
}