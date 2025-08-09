package me.kafae.vitalscorev1.mixin;

import me.kafae.vitalscorev1.Main;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
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

    @Shadow public abstract void offerOrDrop(ItemStack stack);

    @Inject(
            method = "insertStack(Lnet/minecraft/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onInjectInsertStack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        // remove neth
        if (!Main.getConfigHandler().getConfig().getAllowNethArmorAndSword()) {
            if (Main.neth.contains(stack.getItem())) {
                cir.setReturnValue(false);
            }
        }

        // check mace
        if (stack.isOf(Items.MACE)) {
            if (!stack.getComponents().contains(DataComponentTypes.PROFILE)) {
                cir.setReturnValue(false);
            }
        }

        // check name
        if (stack.getCustomName() == null) {
            return;
        }

        // convert knowledge book
        if (Objects.equals(Objects.requireNonNull(stack.getCustomName()).getLiteralString(), "regeneration_shard")) {
            cir.setReturnValue(false);
            this.offerOrDrop(new RegenerationShard().getItem(stack.getCount()));
        }
    }

    @Inject(
            method = "setStack",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onInjectSetStack(int slot, ItemStack stack, CallbackInfo ci) {
        // remove neth
        if (!Main.getConfigHandler().getConfig().getAllowNethArmorAndSword()) {
            if (Main.neth.contains(stack.getItem())) {
                ci.cancel();
            }
        }

        // check mace
        if (stack.isOf(Items.MACE)) {
            if (!stack.getComponents().contains(DataComponentTypes.PROFILE)) {
                ci.cancel();
            }
        }

        // check name
        if (stack.getCustomName() == null) {
            return;
        }

        // convert knowledge book
        if (Objects.equals(Objects.requireNonNull(stack.getCustomName()).getLiteralString(), "regeneration_shard")) {
            ci.cancel();
            this.offerOrDrop(new RegenerationShard().getItem(stack.getCount()));
        }
    }

}
