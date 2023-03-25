package earth.terrarium.spirit.fabric;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.handlers.MobCrystalHandler;
import earth.terrarium.spirit.common.handlers.SoulAbsorptionHandler;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;

public class SpiritFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Spirit.init();
        ServerLivingEntityEvents.AFTER_DEATH.register(SoulAbsorptionHandler::onEntityDeath);
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> MobCrystalHandler.mobInteraction(entity, player, hand));
        registerCreativeTabs();
    }

    public static void registerCreativeTabs() {
        SpiritItems.onRegisterCreativeTabs((loc, item, items) -> FabricItemGroup.builder(loc)
                .title(Component.translatable("itemGroup." + loc.getNamespace() + "." + loc.getPath()))
                .icon(() -> item.get().getDefaultInstance())
                .displayItems((itemDisplayParameters, output) -> items.forEach(output::accept))
                .build());
    }


}