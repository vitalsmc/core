package me.kafae.vitalscorev1.mixin;

import me.kafae.vitalscorev1.Main;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Redirect(
            method = "heal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V"
            )
    )
    private void redirectHeal(LivingEntity instance, float health) {
        if (instance instanceof ServerPlayerEntity) {
            Main.Companion.getLogger().info("lol");
            Float multiplier = Main.Companion.getDataHandler().profiles.get(instance.getUuid().toString()).getRm();
            Float amount = health - instance.getHealth();
            float newAmount = amount * multiplier;
            instance.setHealth(Math.min(instance.getHealth() + newAmount, instance.getMaxHealth()));
        } else {
            instance.setHealth(health);
        }
    }

}
