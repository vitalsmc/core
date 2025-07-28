package me.kafae.vitalscorev1.mixin;

import me.kafae.vitalscorev1.Main;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    @Shadow @Final public PlayerEntity player;

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

        if (stack.getCustomName() == null) {
            return;
        }

        if (Objects.equals(Objects.requireNonNull(stack.getCustomName()).getLiteralString(), "regeneration_shard")) {
            cir.setReturnValue(false);
            player.giveItemStack(new RegenerationShard().getItem(stack.getCount()));
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

        if (stack.getCustomName() == null) {
            return;
        }

        if (Objects.equals(Objects.requireNonNull(stack.getCustomName()).getLiteralString(), "regeneration_shard")) {
            ci.cancel();
            player.giveItemStack(new RegenerationShard().getItem(stack.getCount()));
        }
    }

}
