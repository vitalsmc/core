package me.kafae.vitalscore.events

import me.kafae.vitalscore.Main
import me.kafae.vitalscore.items.head.RegenerationShard
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text


object PlayerJoinHandler {

    // register event
    fun register() {
        ServerPlayConnectionEvents.JOIN.register { h, _, _ ->
            onPlayerJoin(h.player)
        }
    }

    // event logic
    private fun onPlayerJoin(p: ServerPlayerEntity) {
        Main.profiles[p.uuid.toString()] = Main.dsh.load(p.uuid.toString())
        Main.logger.info("Successfully loaded data of player of UUID ${p.uuid}")
        p.sendMessage(Text.literal("§f§a§i§r§x§a§e§r§o")) // xaero minimap fair play

        // testing only
        p.inventory.clear()
        RegenerationShard.give(p, 1)
    }

}