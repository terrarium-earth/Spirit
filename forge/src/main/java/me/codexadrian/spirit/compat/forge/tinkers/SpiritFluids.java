package me.codexadrian.spirit.compat.forge.tinkers;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.platform.forge.ForgeRegistryHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.FluidObject;

import static me.codexadrian.spirit.Spirit.SPIRIT;

public class SpiritFluids {

    public static final RegistryObject<ForgeFlowingFluid.Source> MOLTEN_SOUL_STEEL = ForgeRegistryHelper.FLUIDS.register("molten_soul_steel", () -> new ForgeFlowingFluid.Source(getProperties()));

    public static final RegistryObject<ForgeFlowingFluid.Flowing> FLOWING_MOLTEN_SOUL_STEEL = ForgeRegistryHelper.FLUIDS.register("flowing_molten_soul_steel", () -> new ForgeFlowingFluid.Flowing(getProperties()));

    public static final RegistryObject<LiquidBlock> MOLTEN_SOUL_STEEL_BLOCK = ForgeRegistryHelper.BLOCKS.register("molten_soul_steel_block", () -> new LiquidBlock(MOLTEN_SOUL_STEEL, Block.Properties.of(Material.LAVA).lightLevel(value -> 15).randomTicks().strength(100.0F).noDrops()));

    public static final RegistryObject<BucketItem> MOLTEN_SOUL_STEEL_BUCKET = ForgeRegistryHelper.ITEMS.register("molten_soul_steel_bucket", () -> new BucketItem(MOLTEN_SOUL_STEEL, new Item.Properties().tab(SPIRIT).craftRemainder(Items.BUCKET).stacksTo(1)));

    public static final FluidObject<ForgeFlowingFluid> MOLTEN_SOUL_STEEL_FLUID = new FluidObject<>(new ResourceLocation(Spirit.MODID, "soul_steel"), "soul_steel", MOLTEN_SOUL_STEEL, FLOWING_MOLTEN_SOUL_STEEL, MOLTEN_SOUL_STEEL_BLOCK);
    public static ForgeFlowingFluid.Properties getProperties() {
        return new ForgeFlowingFluid.Properties(MOLTEN_SOUL_STEEL, FLOWING_MOLTEN_SOUL_STEEL, FluidAttributes.builder(new ResourceLocation(Spirit.MODID, "block/fluid/molten_soul_steel_still"), new ResourceLocation(Spirit.MODID, "block/fluid/molten_soul_steel_flow"))
                .overlay(new ResourceLocation(Spirit.MODID, "block/fluid/molten_soul_steel_still"))
                .luminosity(15)
                .density(3000)
                .viscosity(6000)
                .temperature(1000)
                .sound(SoundEvents.BUCKET_FILL_LAVA, SoundEvents.BUCKET_EMPTY_LAVA)
        );
    }

    public static void register() {}
}
