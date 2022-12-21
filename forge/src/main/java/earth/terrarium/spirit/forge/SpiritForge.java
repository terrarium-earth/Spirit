package earth.terrarium.spirit.forge;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.handlers.MobCrystalHandler;
import earth.terrarium.spirit.common.handlers.SoulAbsorptionHandler;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.core.net.Priority;

@Mod(Spirit.MODID)
public class SpiritForge {
    public static CreativeModeTab CREATIVE_TAB;

    public SpiritForge() {
        Spirit.init();
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onItemUse);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::afterDeath);
    }

    @SubscribeEvent
    public void registerContents(CreativeModeTabEvent.BuildContents event) {
        SpiritItems.ITEMS.getEntries().forEach((item) -> event.registerSimple(CREATIVE_TAB, item.get()));
    }

    @SubscribeEvent
    public void registerTab(CreativeModeTabEvent.Register event) {
        CREATIVE_TAB = event.registerCreativeModeTab(new ResourceLocation(Spirit.MODID, "spirit"), builder -> builder.title(Component.translatable("creative_tab.spirit")).icon(() -> SpiritItems.SOUL_CRYSTAL.get().getDefaultInstance()));
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