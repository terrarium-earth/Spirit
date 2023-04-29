package earth.terrarium.spirit.client.renderer.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartNames;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SoulSteelArmorModel extends HumanoidModel<LivingEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Spirit.MODID, "soul_steel_armor"), "main");

    private final HumanoidModel<LivingEntity> contextModel;
    private final ModelPart leggings;
    private final ModelPart rightBoot;
    private final ModelPart leftBoot;
    @Nullable
    private final ResourceLocation innerTexture;
    private final ColorPalette color;

    public SoulSteelArmorModel(ModelPart root, HumanoidModel<LivingEntity> contextModel, LivingEntity entity, EquipmentSlot slot, ItemStack stack) {
        super(root);
        this.contextModel = contextModel;
        this.leggings = root.getChild("leggings");
        this.rightBoot = root.getChild("left_boot");
        this.leftBoot = root.getChild("right_boot");

        if (stack.getItem() instanceof SoulSteelArmor item) {
            this.innerTexture = item.getUnderlayTexture(stack, entity, slot, null);
            ArmorAbility ability = item.getAbility(stack);
            if (ability == null) {
                this.color = new ColorPalette(Spirit.SOUL_COLOR);
            } else {
                this.color = ability.getColor();
            }
        } else {
            this.innerTexture = null;
            this.color = new ColorPalette(Spirit.SOUL_COLOR);
        }

        setVisible(slot);
    }

    @SuppressWarnings("unused")
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F))
                .texOffs(32, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition hat = partdefinition.addOrReplaceChild(PartNames.HAT, CubeListBuilder.create().texOffs(0, 0), PartPose.ZERO);

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(1.0F))
                .texOffs(24, 0).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition cube_r1 = right_arm.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 0).addBox(-4.25F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F))
                .texOffs(16, 48).addBox(-4.25F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(1.25F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition cube_r2 = left_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(16, 48).mirror().addBox(0.25F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false)
                .texOffs(48, 0).mirror().addBox(0.25F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offsetAndRotation(-1.25F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition right_boot = partdefinition.addOrReplaceChild("right_boot", CubeListBuilder.create().texOffs(0, 48).addBox(-2.2F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition left_boot = partdefinition.addOrReplaceChild("left_boot", CubeListBuilder.create().texOffs(0, 48).mirror().addBox(-1.8F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(1.0F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        PartDefinition leggings = partdefinition.addOrReplaceChild("leggings", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -0.28F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(16, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F))
                .texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.7F)), PartPose.offset(-2.0F, 12.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.7F)).mirror(false)
                .texOffs(16, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.6F)).mirror(false), PartPose.offset(2.0F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, @NotNull VertexConsumer vertices, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.attackTime = this.contextModel.attackTime;
        this.riding = this.contextModel.riding;
        this.young = this.contextModel.young;
        this.leftArmPose = this.contextModel.leftArmPose;
        this.rightArmPose = this.contextModel.rightArmPose;
        this.crouching = this.contextModel.crouching;

        this.head.copyFrom(this.contextModel.head);
        this.body.copyFrom(this.contextModel.body);
        this.rightArm.copyFrom(this.contextModel.rightArm);
        this.leftArm.copyFrom(this.contextModel.leftArm);
        this.leggings.copyFrom(this.contextModel.body);
        this.leftLeg.copyFrom(this.contextModel.leftLeg);
        this.rightLeg.copyFrom(this.contextModel.rightLeg);
        this.rightBoot.copyFrom(this.contextModel.rightLeg);
        this.leftBoot.copyFrom(this.contextModel.leftLeg);

        try (var ignored = new CloseablePoseStack(poseStack)) {
            if (this.young) {
                poseStack.scale(0.5f, 0.5f, 0.5f);
                poseStack.translate(0, 1.5f, 0);
            }

            // outer layer
            renderParts(poseStack, packedLight, packedOverlay, vertices, 0xFFFFFF);

            // inner glowing layer
            if (innerTexture != null) {
                MultiBufferSource provider = Minecraft.getInstance().renderBuffers().bufferSource();
                for (int i = 1; i <= 3; i++) {
                    poseStack.pushPose();
                    ResourceLocation resourceLocation = innerTexture.withPath(innerTexture.getPath() + "_" + i + ".png");
                    VertexConsumer buffer = provider.getBuffer(RenderType.entityTranslucent(resourceLocation));
                    renderParts(poseStack, 0xFF00F0, packedOverlay, buffer, color.asArray()[i - 1]);
                    poseStack.popPose();
                }
            }
        }
    }

    private void renderParts(PoseStack poseStack, int packedLight, int packedOverlay, VertexConsumer buffer, int color) {
        float r = FastColor.ARGB32.red(color) / 255f;
        float g = FastColor.ARGB32.green(color) / 255f;
        float b = FastColor.ARGB32.blue(color) / 255f;
        float a = 1;
        this.head.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        this.body.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        this.leggings.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        this.rightLeg.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        this.leftLeg.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        this.rightBoot.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
        this.leftBoot.render(poseStack, buffer, packedLight, packedOverlay, r, g, b, a);
    }

    private void setVisible(EquipmentSlot slot) {
        this.setAllVisible(false);
        switch (slot) {
            case HEAD -> this.head.visible = true;
            case CHEST -> {
                this.body.visible = true;
                this.rightArm.visible = true;
                this.leftArm.visible = true;
            }
            case LEGS -> {
                this.leggings.visible = true;
                this.rightLeg.visible = true;
                this.leftLeg.visible = true;
            }
            case FEET -> {
                this.rightBoot.visible = true;
                this.leftBoot.visible = true;
            }
            default -> {
            }
        }
    }

    @Override
    public void setAllVisible(boolean visible) {
        super.setAllVisible(visible);
        this.leggings.visible = visible;
        this.rightBoot.visible = visible;
        this.leftBoot.visible = visible;
    }
}
