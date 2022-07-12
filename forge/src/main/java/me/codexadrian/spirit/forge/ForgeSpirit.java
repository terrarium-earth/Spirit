package me.codexadrian.spirit.forge;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.compat.forge.TOPCompat;
import me.codexadrian.spirit.compat.forge.TinkersCompat;
import me.codexadrian.spirit.compat.forge.tinkers.SpiritFluids;
import me.codexadrian.spirit.compat.forge.tinkers.data.*;
import me.codexadrian.spirit.entity.CrudeSoulEntity;
import me.codexadrian.spirit.items.SoulMetalMaterial;
import me.codexadrian.spirit.platform.forge.ForgeRegistryHelper;
import me.codexadrian.spirit.registry.SpiritMisc;
import me.codexadrian.spirit.registry.SpiritRecipes;
import me.codexadrian.spirit.registry.forge.SpiritRecipesImpl;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.List;

@Mod(Spirit.MODID)
public class ForgeSpirit {

    public ForgeSpirit() {
        SpiritRecipes.registerAll();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SpiritConfigImpl.CONFIG, "spirit.toml");
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TierSortingRegistry.registerTier(SoulMetalMaterial.INSTANCE, new ResourceLocation(Spirit.MODID, "soul_metal"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND));
        SpiritRecipesImpl.RECIPE_TYPES.register(eventBus);
        Spirit.onInitialize();
        SpiritFluids.register();
        ForgeRegistryHelper.BLOCKS.register(eventBus);
        ForgeRegistryHelper.ITEMS.register(eventBus);
        ForgeRegistryHelper.BLOCK_ENTITIES.register(eventBus);
        ForgeRegistryHelper.ENCHANTMENTS.register(eventBus);
        ForgeRegistryHelper.ENTITIES.register(eventBus);
        ForgeRegistryHelper.RECIPE_SERIALIZERS.register(eventBus);
        ForgeRegistryHelper.FLUIDS.register(eventBus);
        eventBus.addListener(this::entityAttributeStuff);
        eventBus.addListener(this::imcEvent);
        eventBus.addListener(this::gatherData);
    }

    private void imcEvent(InterModEnqueueEvent event) {
        if(ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPCompat::new);
        }
    }

    public void gatherData(GatherDataEvent event) {
        if (ModList.get().isLoaded("tconstruct")) TinkersCompat.gatherData(event);
    }

    private void entityAttributeStuff(EntityAttributeCreationEvent event) {
        event.put(SpiritMisc.SOUL_ENTITY.get(), CrudeSoulEntity.createMobAttributes().build());
    }

    @SubscribeEvent
    public static void registerRecipeTypes(RegistryEvent.Register<RecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Spirit.MODID, "soul_engulfing"), SpiritRecipesImpl.SOUL_ENGULFING);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Spirit.MODID, "soul_cage_tier"), SpiritRecipesImpl.TIER);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Spirit.MODID, "mob_trait"), SpiritRecipesImpl.MOB_TRAIT);
        Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(Spirit.MODID, "soul_transmutation"), SpiritRecipesImpl.SOUL_TRANSMUTATION);
    }
}