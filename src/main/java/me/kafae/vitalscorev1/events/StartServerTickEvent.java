package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public class StartServerTickEvent {

    private static int tick = 0;

    // register
    public void register() {
        ServerTickEvents.START_SERVER_TICK.register(this::onStartServerTick);
    }

    // event logic
    private void onStartServerTick(MinecraftServer s) {
        Main.getCooldownHandler().reduceCooldown();

        if (tick < 20) {
            tick++;
            return;
        }

        s.getPlayerManager().getPlayerList().forEach(p -> {
            try {
                p.sendMessage(Text.literal("§c" + Main.getDataHandler().profiles.get(p.getUuid().toString()).getRm() + "x"), true);
            } catch (Exception e) {
                p.networkHandler.disconnect(Text.literal("An unexpected error has occurred, please contact server staff"));
            }

        });
        tick = 0;
    }

}
