package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import me.kafae.vitalscorev1.items.mace.Mace;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ServerPlayerJoinEvent {

    // register event
    public void register() {
        ServerPlayConnectionEvents.JOIN.register((h, p, s) -> {
            onServerPlayerJoin(h.player);
        });
    }

    // event logic
    private void onServerPlayerJoin(ServerPlayerEntity p) {
        Main.getDataHandler().loadProfile(p.getUuid().toString());
        Main.getLogger().info("Successfully loaded data of player of UUID %s".formatted(p.getUuid().toString()));

        // xaero minimap fairplay
        p.sendMessage(Text.literal("§f§a§i§r§x§a§e§r§o"));

        // testing only
        if (Main.getConfigHandler().getConfig().getIsTesting()) {
            p.getInventory().clear();
            p.giveItemStack(new RegenerationShard().getItem(1));
            p.giveItemStack(new Mace().getItem(1));
        }
    }

}
