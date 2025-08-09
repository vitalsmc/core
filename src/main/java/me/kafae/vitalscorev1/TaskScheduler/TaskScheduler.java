package me.kafae.vitalscorev1.TaskScheduler;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskScheduler {
    private static final ConcurrentHashMap<Runnable, Integer> delayedTasks = new ConcurrentHashMap<>();

    public static void runTaskLater(MinecraftServer server, Runnable task, long delayTicks) {
        delayedTasks.put(task, (int) delayTicks);
    }

    public static void register() {
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            delayedTasks.forEach((task, ticksLeft) -> {
                if (ticksLeft <= 0) {
                    server.execute(task);
                    delayedTasks.remove(task);
                } else {
                    delayedTasks.put(task, ticksLeft - 1);
                }
            });
        });
    }
}