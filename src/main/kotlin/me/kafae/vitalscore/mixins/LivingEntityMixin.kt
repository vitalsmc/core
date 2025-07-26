package me.kafae.vitalscore.mixins

import me.kafae.vitalscore.Main
import net.minecraft.entity.LivingEntity
import net.minecraft.server.network.ServerPlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect


@Mixin(LivingEntity::class)
abstract class LivingEntityMixin {
    @Redirect(
        method = ["heal"],
        at = At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"
        )
    )
    private fun modifyHealingAmount(entity: LivingEntity, newHealth: Float) {
        val modifiedAmount = if (entity is ServerPlayerEntity) {
            val originalHealing = newHealth - entity.health
            val adjustedHealing = originalHealing * Main.profiles[entity.uuid.toString()]!!.rm
            entity.health + adjustedHealing
        } else {
            newHealth
        }
        entity.health = modifiedAmount.coerceIn(0f, entity.maxHealth)
    }
}