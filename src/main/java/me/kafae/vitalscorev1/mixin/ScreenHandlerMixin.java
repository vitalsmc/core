package me.kafae.vitalscorev1.mixin;

import me.kafae.vitalscorev1.Main;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {

    @Shadow public abstract DefaultedList<ItemStack> getStacks();

    @Unique
    private boolean isPlayerViewingEnderChest(PlayerEntity player) {
        if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
            return player.currentScreenHandler.slots.stream()
                    .anyMatch(slot -> slot.inventory instanceof EnderChestInventory);
        }
        return false;
    }

    @Inject(
            method = "onSlotClick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (!Main.getConfigHandler().getConfig().getAllowSpecialItemsInEnderChest()) {
            if (isPlayerViewingEnderChest(player)) {
                if (Main.bannedEChestItems.contains(this.getStacks().get(slotIndex).getItem())) {
                    ci.cancel();
                    player.getInventory().offerOrDrop(this.getStacks().get(slotIndex));
                }
            }
        }
    }

}
