package earth.terrarium.spirit.forge;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.armor.BlankArmorAbility;
import earth.terrarium.spirit.api.abilities.tool.BlankToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbilityManager;
import earth.terrarium.spirit.client.forge.SpiritClientForge;
import earth.terrarium.spirit.common.handlers.MobCrystalHandler;
import earth.terrarium.spirit.common.handlers.SoulAbsorptionHandler;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;

@Mod(Spirit.MODID)
public class SpiritForge {
    public SpiritForge() {
        DeferredRegister<ArmorAbility> armorAbilityRegistry = DeferredRegister.create(ArmorAbilityManager.ARMOR_ABILITY_REGISTRY_KEY, Spirit.MODID);
        DeferredRegister<ToolAbility> toolAbilityRegistry = DeferredRegister.create(ToolAbilityManager.TOOL_ABILITY_REGISTRY_KEY, Spirit.MODID);
        armorAbilityRegistry.makeRegistry(() -> new RegistryBuilder<ArmorAbility>().setDefaultKey(ArmorAbilityManager.NO_ABILITY));
        toolAbilityRegistry.makeRegistry(() -> new RegistryBuilder<ToolAbility>().setDefaultKey(ToolAbilityManager.NO_ABILITY));
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        armorAbilityRegistry.register("no_ability", BlankArmorAbility::new);
        armorAbilityRegistry.register(bus);
        toolAbilityRegistry.register("no_ability", BlankToolAbility::new);
        toolAbilityRegistry.register(bus);
        Spirit.init();
        bus.addListener(SpiritForge::commonSetup);
        bus.addListener(SpiritForge::onRegisterCreativeTabs);
        MinecraftForge.EVENT_BUS.addListener(this::onItemUse);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::afterDeath);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> SpiritClientForge::init);
    }

    public static void commonSetup(FMLCommonSetupEvent event) {
        Spirit.postInit();
    }

    public static void onRegisterCreativeTabs(CreativeModeTabEvent.Register event) {
        SpiritItems.onRegisterCreativeTabs((loc, item, items) -> event.registerCreativeModeTab(loc, b -> b
                .title(Component.translatable("itemGroup." + loc.getNamespace() + "." + loc.getPath()))
                .icon(() -> item.get().getDefaultInstance())
                .displayItems((featureFlagSet, output) -> items.forEach(output::accept))
                .build()));
    }

    public void afterDeath(LivingDeathEvent event) {
        if (!event.isCanceled()) {
            SoulAbsorptionHandler.onEntityDeath(event.getEntity(), event.getSource());
        }
    }

    public void onItemUse(PlayerInteractEvent.EntityInteract event) {
        if (!event.isCanceled()) {
            MobCrystalHandler.mobInteraction(event.getTarget(), event.getEntity(), event.getHand());
        }
    }
}