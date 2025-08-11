package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class StartServerTickEvent {

    private static int tick = 0;
    private static int save = 0;

    // register
    public void register() {
        ServerTickEvents.START_SERVER_TICK.register(this::onStartServerTick);
    }

    // event logic
    private void onStartServerTick(MinecraftServer s) {
        s.execute(() -> {
            Main.server = s;

            if (tick < 20) {
                tick++;
                return;
            }

            // runs every SECOND
            CompletableFuture.runAsync(() -> Main.getCooldownHandler().reduceCooldown(), s);

            List<ServerPlayerEntity> plrs = new ArrayList<>(s.getPlayerManager().getPlayerList());

            plrs.forEach(p -> {
                try {
                    p.sendMessage(Text.literal("§c" + Main.getDataHandler().profiles.get(p.getUuid().toString()).getRm() + "x"), true);
                } catch (Exception e) {
                    p.networkHandler.disconnect(Text.literal("An unexpected error has occurred, please try to rejoin"));
                }

            });
            tick = 0;

            if (save < Main.getConfigHandler().getConfig().getBackupInterval()) {
                save++;
            } else {
                CompletableFuture.runAsync(() -> {
                    plrs.forEach(p -> {
                        Main.getDataHandler().saveProfile(p.getUuid().toString(), true);
                    });
                }, s);
                save = 0;
            }
        });
    }

}
