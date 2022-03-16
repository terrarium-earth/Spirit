package me.codexadrian.spirit;

import me.codexadrian.spirit.blocks.soulcage.SoulCageBlock;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlockEntity;
import me.codexadrian.spirit.items.DivineCrystalItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static me.codexadrian.spirit.Spirit.MODID;

public class FabricSpirit implements ModInitializer {
    public static final CreativeModeTab SPIRIT = FabricItemGroupBuilder.create(new ResourceLocation(MODID, "spirit")).icon(() -> new ItemStack(FabricSpirit.SOUL_CAGE)).build();
    public static final SoulCageBlock SOUL_CAGE = new SoulCageBlock(FabricBlockSettings.copyOf(Blocks.SPAWNER).requiresCorrectToolForDrops());
    public static final BlockEntityType<SoulCageBlockEntity> SOUL_CAGE_ENTITY = FabricBlockEntityTypeBuilder.create(SoulCageBlockEntity::new, SOUL_CAGE).build(null);
    public static final BlockItem SOUL_CAGE_ITEM = new BlockItem(SOUL_CAGE, new Item.Properties().tab(FabricSpirit.SPIRIT).rarity(Rarity.EPIC));
    public static final DivineCrystalItem SOUL_CRYSTAL = new DivineCrystalItem(new Item.Properties().tab(FabricSpirit.SPIRIT).stacksTo(1).rarity(Rarity.RARE));
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Block BROKEN_SPAWNER = new Block(FabricBlockSettings.copyOf(Blocks.SPAWNER).requiresCorrectToolForDrops());
    public static final BlockItem BROKEN_SPAWNER_ITEM = new BlockItem(BROKEN_SPAWNER, new Item.Properties().tab(FabricSpirit.SPIRIT).rarity(Rarity.EPIC));
    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new ResourceLocation(MODID, "soul_cage"), SOUL_CAGE);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(MODID, "soul_cage"), SOUL_CAGE_ENTITY);
        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "soul_cage"), SOUL_CAGE_ITEM);
        Registry.register(Registry.BLOCK, new ResourceLocation(MODID, "broken_spawner"), BROKEN_SPAWNER);
        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "broken_spawner"), BROKEN_SPAWNER_ITEM);
        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "soul_crystal"), SOUL_CRYSTAL);
    }
}
