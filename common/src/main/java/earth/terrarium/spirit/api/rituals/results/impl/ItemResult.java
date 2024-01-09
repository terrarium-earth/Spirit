package earth.terrarium.spirit.api.rituals.results.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.rituals.results.RitualResult;
import earth.terrarium.spirit.api.rituals.results.RitualResultSerializer;
import earth.terrarium.spirit.compat.rei.ComponentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.NotImplementedException;

public record ItemResult(ItemStack item) implements RitualResult<ItemResult> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public ItemStack getItemRepresentation() {
        return item.copy();
    }

    @Override
    public void onRitualComplete(Level level, BlockPos blockPos, ItemStack catalyst) {
        if (hasContainerBelow(level, blockPos)) {
            if (!insertItemIntoContainerBelow(level, blockPos, item.copy())) {
                Block.popResource(level, blockPos, item.copy());
            }
        } else {
            Block.popResource(level, blockPos, item.copy());
        }
    }

    @Override
    public ComponentUtils.ReiPlacer getREIPlacer() {
        return ComponentUtils.itemOutputPlacer(this);
    }

    @Override
    public RitualResultSerializer<ItemResult> getSerializer() {
        return SERIALIZER;
    }

    @ExpectPlatform
    public static boolean hasContainerBelow(Level level, BlockPos blockPos) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static boolean insertItemIntoContainerBelow(Level level, BlockPos blockPos, ItemStack itemStack) {
        throw new NotImplementedException();
    }

    public static class Serializer implements RitualResultSerializer<ItemResult> {
        public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "item");
        public static final Codec<ItemResult> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ItemStackCodec.CODEC.fieldOf("item").forGetter(ItemResult::item)
        ).apply(instance, ItemResult::new));

        @Override
        public Codec<ItemResult> codec() {
            return CODEC;
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
}
