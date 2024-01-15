package earth.terrarium.spirit.api.rituals.components.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.rituals.components.RitualComponent;
import earth.terrarium.spirit.api.rituals.components.RitualComponentSerializer;
import earth.terrarium.spirit.api.souls.InteractionMode;
import earth.terrarium.spirit.api.souls.SoulApi;
import earth.terrarium.spirit.api.souls.base.SoulContainer;
import earth.terrarium.spirit.api.souls.base.SoulContainingBlock;
import earth.terrarium.spirit.api.souls.util.SoulIngredient;
import earth.terrarium.spirit.api.souls.stack.SoulStack;
import earth.terrarium.spirit.common.registry.SpiritItems;
import earth.terrarium.spirit.compat.rei.ComponentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public record SoulComponent(SoulIngredient soulIngredient) implements RitualComponent<SoulComponent> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public boolean matches(Level level, BlockPos blockPos, BlockPos ritualPos) {
        SoulContainer soulContainer = SoulContainer.of(level, blockPos, null);
        if (soulContainer != null) {
            for (SoulStack stack : soulContainer.getSouls()) {
                if (soulIngredient.test(stack)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onRitualComplete(Level level, BlockPos componentPos, BlockPos ritualPos) {
        SoulContainer soulContainer = SoulContainer.of(level, componentPos, null);
        if (soulContainer != null) {
            for (SoulStack stack : soulContainer.getSouls()) {
                if (soulIngredient.test(stack)) {
                    soulContainer.extract(new SoulStack(stack.getEntity(), 1), InteractionMode.NO_TAKE_BACKSIES);
                    BlockState state = level.getBlockState(componentPos);
                    level.sendBlockUpdated(componentPos, state, state, Block.UPDATE_ALL);
                    return;
                }
            }
        }
    }

    @Override
    public List<Ingredient> getIngredients() {
        return List.of(Ingredient.of(SpiritItems.SOUL_CRYSTAL.get()));
    }

    @Override
    public ComponentUtils.ReiPlacer getREIPlacer() {
        return ComponentUtils.soulInputPlacer(this);
    }

    @Override
    public RitualComponentSerializer<SoulComponent> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements RitualComponentSerializer<SoulComponent> {
        public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "soul");
        public static final Codec<SoulComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SoulIngredient.CODEC.fieldOf("soul").forGetter(component -> component.soulIngredient)
        ).apply(instance, SoulComponent::new));

        @Override
        public ResourceLocation id() {
            return ID;
        }

        @Override
        public Codec<SoulComponent> codec() {
            return CODEC;
        }
    }
}
