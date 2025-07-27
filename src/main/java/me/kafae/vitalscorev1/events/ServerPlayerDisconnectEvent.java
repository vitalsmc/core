package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerPlayerDisconnectEvent {

    // register event
    public void register() {
        ServerPlayConnectionEvents.DISCONNECT.register((h, p) -> {
            onServerPlayerDisconnect(h.player);
        });
    }


    // event logic
    private void onServerPlayerDisconnect(ServerPlayerEntity p) {
        Main.Companion.getDataHandler().saveProfile(p.getUuid().toString());
        Main.Companion.getLogger().info("Successfully saved data of player of UUID {}", p.getUuid().toString());
    }

}
