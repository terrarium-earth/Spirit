package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.elements.BaseElements;
import earth.terrarium.spirit.api.elements.SoulElement;
import earth.terrarium.spirit.common.block.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Supplier;

public class SpiritBlocks {
    public static final ResourcefulRegistry<Block> BLOCKS = ResourcefulRegistries.create(BuiltInRegistries.BLOCK, Spirit.MODID);

    public static final Supplier<Block> SOUL_CAGE = BLOCKS.register("soul_cage", () -> new SoulCageBlock(Block.Properties.of(Material.METAL).strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));

    public static final Supplier<Block> PEDESTAL = BLOCKS.register("pedestal", () -> new PedestalBlock(Block.Properties.of(Material.STONE).strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final Supplier<Block> SUMMONING_PEDESTAL = BLOCKS.register("summoning_pedestal", () -> new SummoningPedestalBlock(Block.Properties.of(Material.STONE).strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));
    public static final Supplier<Block> SOUL_BASIN = BLOCKS.register("soul_basin", () -> new SoulBasinBlock(Block.Properties.of(Material.STONE).strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion()));

    //Elemental fires ember, earth, water, ender
    public static final Supplier<Block> EMBER_FIRE = BLOCKS.register("ember_fire", () -> new RagingSoulFireBlock(BaseElements.EMBER, BlockBehaviour.Properties.of(Material.FIRE, MaterialColor.FIRE).noCollission().instabreak().lightLevel(arg -> 10).sound(SoundType.WOOL)));
    public static final Supplier<Block> EARTH_FIRE = BLOCKS.register("earth_fire", () -> new RagingSoulFireBlock(BaseElements.EARTH, BlockBehaviour.Properties.of(Material.FIRE, MaterialColor.COLOR_GREEN).noCollission().instabreak().lightLevel(arg -> 10).sound(SoundType.WOOL)));
    public static final Supplier<Block> ENDER_FIRE = BLOCKS.register("ender_fire", () -> new RagingSoulFireBlock(BaseElements.ENDER, BlockBehaviour.Properties.of(Material.FIRE, MaterialColor.COLOR_MAGENTA).noCollission().instabreak().lightLevel(arg -> 10).sound(SoundType.WOOL)));
    public static final Supplier<Block> WATER_FIRE = BLOCKS.register("water_fire", () -> new RagingSoulFireBlock(BaseElements.WATER, BlockBehaviour.Properties.of(Material.FIRE, MaterialColor.COLOR_BLUE).noCollission().instabreak().lightLevel(arg -> 10).sound(SoundType.WOOL)));

    static {
        SoulElement.ELEMENTAL_FIRES.put(BaseElements.EMBER, EMBER_FIRE.get());
        SoulElement.ELEMENTAL_FIRES.put(BaseElements.EARTH, EARTH_FIRE.get());
        SoulElement.ELEMENTAL_FIRES.put(BaseElements.ENDER, ENDER_FIRE.get());
        SoulElement.ELEMENTAL_FIRES.put(BaseElements.WATER, WATER_FIRE.get());
    }
}
