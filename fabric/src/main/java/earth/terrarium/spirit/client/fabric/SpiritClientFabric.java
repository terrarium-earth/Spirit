package earth.terrarium.spirit.client.fabric;

import earth.terrarium.spirit.client.SpiritClient;
import net.fabricmc.api.ClientModInitializer;

public class SpiritClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SpiritClient.init();
    }
}
