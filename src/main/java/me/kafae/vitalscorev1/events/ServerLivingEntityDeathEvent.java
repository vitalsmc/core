package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import me.kafae.vitalscorev1.TaskScheduler.TaskScheduler;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import me.kafae.vitalscorev1.items.mace.Mace;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ServerLivingEntityDeathEvent {

    private static final float MIN_RM = 0.5F;
    private static final float MAX_RM = 2.0F;
    private static final float RM_CHANGE = 0.1F;

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
                    if (e == d.getAttacker()) {
                        ((ServerPlayerEntity) e).sendMessage(Text.literal("§4Killing yourself is never the option..."));
                        return;
                    }
                    // player killed player
                    atkRM = Main.getDataHandler().profiles.get(d.getAttacker().getUuid().toString()).getRm();
                    if (vicRM <= MIN_RM) {
                        // victim cant lose shit, so attacker cant gain shit
                        ((ServerPlayerEntity) d.getAttacker()).sendMessage(Text.literal("§aYou killed §e§l%s§r§a, but they had nothing to lose!".formatted(e.getName().getString())));
                        ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died to§e§l%s§r§4, but you have nothing to lose!".formatted(d.getAttacker().getName().getString())));
                    } else {
                        if (atkRM >= MAX_RM) {
                            // attacker cant gain shit, drop shard
                            ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died to §e§l%s§r§4, and lost §c§o0.1x§r§4 multiplier!".formatted(d.getAttacker().getName().getString())));
                            ((ServerPlayerEntity) d.getAttacker()).sendMessage(Text.literal("§aYou killed §e§l%s§r§a, a §d§lRegeneration Shard§r§4 has dropped!".formatted(e.getName().getString())));
                            e.getWorld().spawnEntity(new ItemEntity(e.getWorld(), e.getX(), e.getY(), e.getZ(), new RegenerationShard().getItem(1)));
                            vicRM -= RM_CHANGE;
                        } else {
                            // standard procedure
                            ((ServerPlayerEntity) e).sendMessage(Text.literal("§4You died to §e§l%s§r§4, and lost §c§o0.1x§r§4 multiplier!".formatted(d.getAttacker().getName().getString())));
                            ((ServerPlayerEntity) d.getAttacker()).sendMessage(Text.literal("§aYou killed §e§l%s§r§a, and agained §c§o0.1x§r§a multiplier!".formatted(e.getName().getString())));
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
            if (atkRM != null) {
                Main.getDataHandler().profiles.get(d.getAttacker().getUuid().toString()).setRm(atkRM);
            }
            Main.getDataHandler().profiles.get(e.getUuid().toString()).setRm(vicRM);
        } else if (e instanceof EnderDragonEntity) {
            if (((ServerWorld) e.getWorld()).getEnderDragonFight() != null && !((ServerWorld) e.getWorld()).getEnderDragonFight().hasPreviouslyKilled()) {
                // lock end
                Main.allowEndEscape = false;
                e.getWorld().getPlayers().forEach(p -> {
                    p.sendMessage(Text.literal("§4The End has been locked for 5 minutes!"), false);
                });

                // unlock end
                TaskScheduler.runTaskLater(e.getServer(), () -> {
                    Main.allowEndEscape = true;
                    e.getWorld().getPlayers().forEach(p -> {
                        p.sendMessage(Text.literal("§aThe End has been unlocked!"), false);
                    });
                }, (5L * 60L * 20L));

                // drop mace after drag death
                e.getWorld().spawnEntity(new ItemEntity(
                        e.getWorld(),
                        e.getX(),
                        e.getY(),
                        e.getZ(),
                        new Mace().getItem(1)
                ));
                e.getWorld().getPlayers().forEach(p -> {
                    p.sendMessage(Text.literal("§dThe dragon has dropped the mace!"), false);
                });
            }
        }
    }

}
