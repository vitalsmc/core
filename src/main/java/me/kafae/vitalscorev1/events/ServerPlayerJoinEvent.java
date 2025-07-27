package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

public class ServerPlayerJoinEvent {

    // register event
    public void register() {
        ServerPlayConnectionEvents.JOIN.register((h, p, s) -> {
            onServerPlayerJoin(h.player);
        });
    }

    // event logic
    private void onServerPlayerJoin(ServerPlayerEntity p) {
        Main.Companion.getDataHandler().loadProfile(p.getUuid().toString());
        Main.Companion.getLogger().info("Successfully loaded data of player of UUID {}", p.getUuid().toString());

        // testing only
        p.getInventory().clear();
        p.giveItemStack(new RegenerationShard().getItem(1));
    }

}
