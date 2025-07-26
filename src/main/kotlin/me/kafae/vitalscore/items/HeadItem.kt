package me.kafae.vitalscore.items

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.LoreComponent
import net.minecraft.component.type.ProfileComponent
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import java.util.UUID

abstract class HeadItem {

    private val spacing: String = "§7-------------------------"

    abstract val id: String
    abstract val display: String
    abstract val lore: List<String>
    abstract val b64texture: String

    fun item(n: Int): ItemStack {
        val stack: ItemStack = ItemStack(Items.PLAYER_HEAD, n)

        // set name
        stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal(display))

        //set lore
        val lorelist: MutableList<Text> = mutableListOf()
        lorelist.addLast(Text.literal(spacing))
        lorelist.addLast(Text.literal(" "))
        lore.forEach { s ->
            lorelist.addLast(Text.literal("§7$s"))
        }
        lorelist.addLast(Text.literal(" "))
        lorelist.addLast(Text.literal("§7ID: $id"))
        stack.set(DataComponentTypes.LORE, LoreComponent(lorelist))

        // profiles
        val profid: UUID = UUID.randomUUID()
        val gameprof: GameProfile = GameProfile(profid, "headitem")

        // add texture
        gameprof.properties.put("textures", Property("textures", b64texture))

        // prof comp
        val profcomp: ProfileComponent = ProfileComponent(gameprof)

        // ppc
        stack.set(DataComponentTypes.PROFILE, profcomp)

        return stack
    }

    fun give(p: ServerPlayerEntity, n: Int) {
        p.giveItemStack(item(n))
    }

}