package me.kafae.vitalscore.items.head

import me.kafae.vitalscore.items.HeadItem

object RegenerationShard : HeadItem() {

    override val id: String
        get() = "regeneration_shard"
    override val display: String
        get() = "§d§ke§r §cRegeneration Shard §r§d§ke"
    override val lore: List<String>
        get() = listOf(
            "§6§lRight Click: §4Consume",
            "Gain §c+0.1x§r §7regeneration",
            "multiplier."
        )
    override val b64texture: String
        get() = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2ViM2Q3MWNkNWFjOTFkOGJkMWQ2MWZjZWI1NTU3YjllMTljMGI5YTU0ZGMyNzIwZGZmZDVmYmNjMTIyN2ZjMCJ9fX0="

}