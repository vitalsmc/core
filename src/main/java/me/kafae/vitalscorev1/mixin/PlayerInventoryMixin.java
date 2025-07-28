package me.kafae.vitalscorev1.mixin;

import me.kafae.vitalscorev1.Main;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Inject(
            method = "insertStack(Lnet/minecraft/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onInjectInsertStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!Main.getConfigHandler().getConfig().getAllowNethArmorAndSword()) {
            if (Main.neth.contains(stack.getItem())) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(
            method = "setStack",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onInjectSetStack(int slot, ItemStack stack, CallbackInfo ci) {
        if (!Main.getConfigHandler().getConfig().getAllowNethArmorAndSword()) {
            if (Main.neth.contains(stack.getItem())) {
                ci.cancel();
            }
        }
    }

}
