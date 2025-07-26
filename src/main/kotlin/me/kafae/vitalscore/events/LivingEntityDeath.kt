package me.kafae.vitalscore.events

import me.kafae.vitalscore.Main
import me.kafae.vitalscore.items.head.RegenerationShard
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

object LivingEntityDeath {

    // register event
    fun register() {
        ServerLivingEntityEvents.AFTER_DEATH.register { e, d ->
            onLivingEntityDeath(e, d)
        }
    }

    // event logic
    private fun onLivingEntityDeath(e: LivingEntity, d: DamageSource) {
        if (e is ServerPlayerEntity && d.attacker != null) {
            if (d.attacker is ServerPlayerEntity) {
                if (Main.profiles[e.uuid.toString()]!!.rm > 0.5f) {
                    Main.profiles[e.uuid.toString()]!!.rm -= 0.5f
                    e.sendMessage(Text.literal("§4You died to §e§l${(d.attacker!! as ServerPlayerEntity)}§r§4, and lost §c§o0.1x§r§4 multiplier"), false)
                    if (Main.profiles[(d.attacker!! as ServerPlayerEntity).uuid.toString()]!!.rm < 2f) {
                        Main.profiles[(d.attacker!! as ServerPlayerEntity).uuid.toString()]!!.rm += 0.1f
                        (d.attacker!! as ServerPlayerEntity).sendMessage(Text.literal("§4You killed §e§l${e.name}§r§4, and agained §c§o0.1x§r§4 multiplier"), false)
                    } else {
                        e.world.spawnEntity(ItemEntity(e.world, e.pos.x, e.pos.y, e.pos.z, RegenerationShard.item(1)))
                        (d.attacker!! as ServerPlayerEntity).sendMessage(Text.literal("§4You killed §e§l${e.name}§r§4, a §d§lRegeneration Shard§r§4 has dropped!"), false)
                    }
                } else {
                    (d.attacker!! as ServerPlayerEntity).sendMessage(Text.literal("§4You killed §e§l${e.name}§r§4, but they had nothing to lose!"), false)
                    e.sendMessage(Text.literal("§4You died to§e§l${(d.attacker!! as ServerPlayerEntity).name}§r§4, but you have nothing to lose"), false)
                }
            } else {
                e.sendMessage(Text.literal("§4You died of &2&onatural&r&4 causes, a §d&lRegeneration Shard§r§4 has dropped!"), false)
            }
        }
    }
}