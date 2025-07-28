package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ServerLivingEntityDeathEvent {

    private static final float MIN_RM = 0.5f;
    private static final float MAX_RM = 2.0f;
    private static final float RM_CHANGE = 0.1f;

    // register event
    public void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register(this::onServerLivingEntityDeath);
    }

    // event logic
    private void onServerLivingEntityDeath(LivingEntity e, DamageSource d) {
        if (e instanceof ServerPlayerEntity) {
            float vicRM = Main.getDataHandler().profiles.get(e.getUuid().toString()).getRm();
            @Nullable
            Float atkRM = null;
            if (d.getAttacker() != null) {
                if (d.getAttacker() instanceof ServerPlayerEntity) {
                    // player killed player
                    atkRM = Main.getDataHandler().profiles.get(d.getAttacker().getUuid().toString()).getRm();
                    if (vicRM <= MIN_RM) {
                        // victim cant lose shit, so attacker cant gain shit
                        ((ServerPlayerEntity) d.getAttacker()).sendMessage(Text.literal("§4You killed §e§l%s§r§4, but they had nothing to lose!".formatted(e.getName())));
                        ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died to§e§l%s§r§4, but you have nothing to lose!".formatted(d.getName())));
                    } else {
                        if (atkRM >= MAX_RM) {
                            // attacker cant gain shit, drop shard
                            ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died to §e§l%s§r§4, and lost §c§o0.1x§r§4 multiplier!".formatted(d.getName())));
                            ((ServerPlayerEntity) d.getAttacker()).sendMessage(Text.literal("§4You killed §e§l%s§r§4, a §d§lRegeneration Shard§r§4 has dropped!".formatted(e.getName())));
                            e.getWorld().spawnEntity(new ItemEntity(e.getWorld(), e.getX(), e.getY(), e.getZ(), new RegenerationShard().getItem(1)));
                            vicRM -= RM_CHANGE;
                        } else {
                            // standard procedure
                            ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died to §e§l%s§r§4, and lost §c§o0.1x§r§4 multiplier!".formatted(d.getName())));
                            ((ServerPlayerEntity) d.getAttacker()).sendMessage(Text.literal("§4You killed §e§l%s§r§4, and agained §c§o0.1x§r§4 multiplier!".formatted(e.getName())));
                            vicRM -= RM_CHANGE;
                            atkRM += RM_CHANGE;
                        }
                    }
                } else {
                    // player died of natural causes
                    if (vicRM <= MIN_RM) {
                        // victim has nothing to lose
                        ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died of §2§onatural§r§4 causes, but you have nothing to lose!"));
                    } else {
                        // drop shard
                        ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died of §2§onatural§r§4 causes, a §d§lRegeneration Shard§r§4 has dropped!"));
                        e.getWorld().spawnEntity(new ItemEntity(e.getWorld(), e.getX(), e.getY(), e.getZ(), new RegenerationShard().getItem(1)));
                        vicRM -= RM_CHANGE;
                        }
                }
            } else {
                    // player died of natural causes
                    if (vicRM <= MIN_RM) {
                        // victim has nothing to lose
                        ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died of §2§onatural§r§4 causes, but you have nothing to lose!"));
                    } else {
                        // drop shard
                        ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died of §2§onatural§r§4 causes, a §d§lRegeneration Shard§r§4 has dropped!"));
                        e.getWorld().spawnEntity(new ItemEntity(e.getWorld(), e.getX(), e.getY(), e.getZ(), new RegenerationShard().getItem(1)));
                        vicRM -= RM_CHANGE;
                    }
            }

            // set values
            Main.getDataHandler().profiles.get(e.getUuid().toString()).setRm(vicRM);
            if (atkRM != null) {
                Main.getDataHandler().profiles.get(d.getAttacker().getUuid().toString()).setRm(atkRM);
            }
        }
    }

}
