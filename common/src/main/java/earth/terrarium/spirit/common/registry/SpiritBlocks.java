package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.block.PedestalBlock;
import earth.terrarium.spirit.common.block.SoulCageBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class SpiritBlocks {
    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, Spirit.MODID);

    public static final Supplier<Block> SOUL_CAGE = BLOCKS.register("soul_cage", () -> new SoulCageBlock(Block.Properties.of(Material.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final Supplier<Block> PEDESTAL = BLOCKS.register("pedestal", () -> new PedestalBlock(Block.Properties.of(Material.STONE).strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));
}
