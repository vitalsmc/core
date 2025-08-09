package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class ServerLifeCycleStoppingEvent {

    // register
    public void register() {
        ServerLifecycleEvents.SERVER_STOPPING.register((s) -> {
            onServerLifeCycleStopping();
        });
    }

    // event logic
    private void onServerLifeCycleStopping() {
        Main.getConfigHandler().saveConfig();
        Main.getLogger().info("Successfully saved server config");
    }

}
