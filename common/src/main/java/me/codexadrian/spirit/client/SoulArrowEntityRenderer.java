package me.codexadrian.spirit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import me.codexadrian.spirit.Constants;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import me.codexadrian.spirit.platform.ClientServices;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.NotNull;

public class SoulArrowEntityRenderer extends ArrowRenderer<SoulArrowEntity> {

    public static ResourceLocation SOUL_ARROW = new ResourceLocation(Spirit.MODID, "textures/entity/soul_arrow.png");

    public SoulArrowEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull SoulArrowEntity entity) {
        return SoulArrowEntityRenderer.SOUL_ARROW;
    }

    @Override
    public void render(@NotNull SoulArrowEntity abstractArrow, float f, float g, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(g, (abstractArrow).yRotO, (abstractArrow).getYRot()) - 90.0f));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(g, (abstractArrow).xRotO, (abstractArrow).getXRot())));
        float s = (float)(abstractArrow).shakeTime - g;
        if (s > 0.0f) {
            float t = -Mth.sin(s * 3.0f) * s;
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(t));
        }
        poseStack.mulPose(Vector3f.XP.rotationDegrees(45.0f));
        poseStack.scale(0.05625f, 0.05625f, 0.05625f);
        poseStack.translate(-4.0, 0.0, 0.0);
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(ClientServices.SHADERS.getSoulShader(abstractArrow, this));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0f, 0.15625f, -1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625f, 0.15625f, -1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625f, 0.3125f, -1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0f, 0.3125f, -1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0f, 0.15625f, 1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625f, 0.15625f, 1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625f, 0.3125f, 1, 0, 0, i);
        this.vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0f, 0.3125f, 1, 0, 0, i);
        for (int u = 0; u < 4; ++u) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0f));
            this.vertex(matrix4f, matrix3f, vertexConsumer, -8, -2, 0, 0.0f, 0.0f, 0, 1, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 8, -2, 0, 0.5f, 0.0f, 0, 1, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 8, 2, 0, 0.5f, 0.15625f, 0, 1, 0, i);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -8, 2, 0, 0.0f, 0.15625f, 0, 1, 0, i);
        }
        poseStack.popPose();
        super.render(abstractArrow, f, g, poseStack, multiBufferSource, i);
    }
}
