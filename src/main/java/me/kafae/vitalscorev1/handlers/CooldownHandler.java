package me.kafae.vitalscorev1.handlers;

import me.kafae.vitalscorev1.Main;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class CooldownHandler {

    public static int getCooldownAmount(Item i) {
        return getCooldownAmounts().get(i);
    }

    public static HashMap<Item, Integer> getCooldownAmounts() {
        return new HashMap<>() {{
            put(Items.ENDER_PEARL, Main.getConfigHandler().getConfig().getPearlCD());
        }};
    }

    private final Map<Item, Map<ServerPlayerEntity, Integer>> cooldowns = new HashMap<>();

    public boolean inCooldown(Item i, ServerPlayerEntity p) {
        if (cooldowns.containsKey(i)) {
            return cooldowns.get(i).containsKey(p);
        } else {
            return false;
        }
    }

    public void setCooldown(Item i, ServerPlayerEntity p) {
        if (getCooldownAmounts().containsKey(i)) {
            final int cd = getCooldownAmount(i);
            if (cooldowns.containsKey(i)) {
                cooldowns.get(i).put(p, cd);
            } else {
                cooldowns.put(i, new HashMap<>() {{
                    put(p, cd);
                }});
            }
        }
    }

    public int getCooldown(Item i, ServerPlayerEntity p) {
        return cooldowns.get(i).get(p);
    }

    public void reduceCooldown() {
        cooldowns.forEach((i, m) -> {
            m.replaceAll((p, amt) -> amt - 1);
            m.entrySet().removeIf(entry -> entry.getValue() <= 0);
        });
    }

}
