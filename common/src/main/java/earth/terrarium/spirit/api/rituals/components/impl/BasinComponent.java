package earth.terrarium.spirit.api.rituals.components.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.botarium.common.fluid.base.FluidHolder;
import earth.terrarium.botarium.common.fluid.utils.FluidIngredient;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.rituals.components.RitualComponent;
import earth.terrarium.spirit.api.rituals.components.RitualComponentSerializer;
import earth.terrarium.spirit.common.blockentity.SoulBasinBlockEntity;
import earth.terrarium.spirit.compat.rei.ComponentUtils;
import earth.terrarium.spirit.compat.rei.categories.TransmutationRecipeCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

public record BasinComponent(FluidIngredient fluidIngredient, long amount) implements RitualComponent<BasinComponent> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public boolean matches(Level level, BlockPos blockPos, BlockPos ritualPos) {
        if (level.getBlockEntity(blockPos) instanceof SoulBasinBlockEntity blockEntity) {
            for (FluidHolder fluid : blockEntity.getFluidContainer().getFluids()) {
                FluidHolder toExtract = fluid.copyWithAmount(amount);
                FluidHolder extracted = blockEntity.getFluidContainer().extractFluid(toExtract, true);
                if (fluidIngredient.test(extracted) && extracted.matches(toExtract) && extracted.getFluidAmount() == amount) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRitualComplete(Level level, BlockPos componentPos, BlockPos ritualPos) {
        if (level.getBlockEntity(componentPos) instanceof SoulBasinBlockEntity blockEntity) {
            for (FluidHolder fluid : blockEntity.getFluidContainer().getFluids()) {
                FluidHolder toExtract = fluid.copyWithAmount(amount);
                FluidHolder extracted = blockEntity.getFluidContainer().extractFluid(toExtract, true);
                if (fluidIngredient.test(extracted) && extracted.matches(toExtract) && extracted.getFluidAmount() == amount) {
                    blockEntity.getFluidContainer().extractFluid(fluid.copyWithAmount(amount), false);
                    break;
                }
            }
        }
    }

    @Override
    public List<Ingredient> getIngredients() {
        return fluidIngredient.getFluids().stream().map(FluidHolder::getFluid).map(Fluid::getBucket).map(Ingredient::of).toList();
    }

    @Override
    public ComponentUtils.ReiPlacer getREIPlacer() {
        return ComponentUtils.fluidPlacer(this);
    }

    @Override
    public RitualComponentSerializer<BasinComponent> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements RitualComponentSerializer<BasinComponent> {
        public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "fluid");
        public static final Codec<BasinComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                FluidIngredient.CODEC.fieldOf("fluid").forGetter(component -> component.fluidIngredient),
                Codec.LONG.fieldOf("amount").forGetter(component -> component.amount)
        ).apply(instance, BasinComponent::new));


        @Override
        public ResourceLocation id() {
            return ID;
        }

        @Override
        public Codec<BasinComponent> codec() {
            return CODEC;
        }
    }
}
