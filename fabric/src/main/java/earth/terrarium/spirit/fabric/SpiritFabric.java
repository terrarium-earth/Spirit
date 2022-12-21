package earth.terrarium.spirit.fabric;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.handlers.MobCrystalHandler;
import earth.terrarium.spirit.common.handlers.SoulAbsorptionHandler;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.event.interaction.InteractionEventsRouter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.item.CreativeModeTab;

public class SpiritFabric implements ModInitializer {
    CreativeModeTab CREATIVE_TAB = FabricItemGroup.builder(new ResourceLocation(Spirit.MODID, "spirit"))
            .title(Component.translatable("creative_tab.spirit"))
            .icon(() -> SpiritItems.SOUL_CRYSTAL.get().getDefaultInstance())
            .build();

    @Override
    public void onInitialize() {
        Spirit.init();
        ItemGroupEvents.modifyEntriesEvent(CREATIVE_TAB).register((entries) -> SpiritItems.ITEMS.getEntries().forEach((item) -> entries.accept(item.get())));
        ServerLivingEntityEvents.AFTER_DEATH.register(SoulAbsorptionHandler::onEntityDeath);
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> MobCrystalHandler.mobInteraction(entity, player, hand));
    }
}