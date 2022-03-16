package me.codexadrian.spirit;

import me.codexadrian.spirit.blocks.soulcage.SoulCageBlock;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlockEntity;
import me.codexadrian.spirit.items.DivineCrystalItem;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Constants.MODID)
public class ForgeSpirit {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Constants.MODID);

    private static final CreativeModeTab TAB = new CreativeModeTab("spirit") {
        @Override
        public ItemStack makeIcon() {
            return SOUL_CAGE_ITEM.get().getDefaultInstance();
        }
    };

    public static final RegistryObject<SoulCageBlock> SOUL_CAGE = BLOCKS.register("soul_cage", () ->
            new SoulCageBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops()));

    public static final RegistryObject<BlockItem> SOUL_CAGE_ITEM = ITEMS.register("soul_cage", () ->
            new BlockItem(SOUL_CAGE.get(), new Item.Properties().tab(TAB).rarity(Rarity.EPIC)));

    public static final RegistryObject<BlockEntityType<SoulCageBlockEntity>> SOUL_CAGE_ENTITY = BLOCK_ENTITIES
            .register("soul_cage", () -> BlockEntityType.Builder.of(SoulCageBlockEntity::new, SOUL_CAGE.get())
                    .build(null));

    public static final RegistryObject<Block> BROKEN_SPAWNER = BLOCKS.register("broken_spawner", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops()));

    public static final RegistryObject<BlockItem> BROKEN_SPAWNER_ITEM = ITEMS.register("broken_spawner", () ->
            new BlockItem(BROKEN_SPAWNER.get(), new Item.Properties().tab(TAB).rarity(Rarity.EPIC)));

    public static final RegistryObject<DivineCrystalItem> SOUL_CRYSTAL = ITEMS.register("soul_crystal", () ->
            new DivineCrystalItem(new Item.Properties().tab(TAB).stacksTo(1).rarity(Rarity.RARE)));

    public ForgeSpirit() {
        Spirit.onInitialize();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
    }
}