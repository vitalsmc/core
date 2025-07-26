package me.kafae.vitalscore.events

import me.kafae.vitalscore.Main
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.server.network.ServerPlayerEntity

object PlayerQuitHandler {

    // register event
    fun register() {
        ServerPlayConnectionEvents.DISCONNECT.register { h, _ ->
            onPlayerQuit(h.player)
        }
    }

    // event logic
    private fun onPlayerQuit(p: ServerPlayerEntity) {
        Main.dsh.save(p.uuid.toString())
        Main.logger.info("Successfully saved data of player of UUID ${p.uuid}")
    }

}