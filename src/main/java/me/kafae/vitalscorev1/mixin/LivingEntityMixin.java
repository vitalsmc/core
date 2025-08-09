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
    private void onRedirect(LivingEntity instance, float health) {
        if (instance instanceof ServerPlayerEntity) {
            float multiplier = Main.getDataHandler().profiles.get(instance.getUuid().toString()).getRm();
            float amount = health - instance.getHealth();
            float newAmount = amount * multiplier;
            instance.setHealth(Math.min(instance.getHealth() + newAmount, instance.getMaxHealth()));
        } else {
            instance.setHealth(health);
        }
    }

}
