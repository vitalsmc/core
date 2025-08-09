package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.Objects;

public class UseBlockCallbackEvent {

    // register
    public void register() {
        UseBlockCallback.EVENT.register((p, w, h, r) -> onUseBlockCallback(p, h));
    }

    // event logic
    private ActionResult onUseBlockCallback(PlayerEntity p, Hand h) {
        ItemStack stack = p.getStackInHand(h);

        if (!stack.getComponents().contains(DataComponentTypes.PROFILE)) {
            return ActionResult.PASS;
        }

        if (Objects.equals(Objects.requireNonNull(stack.getComponents().get(DataComponentTypes.PROFILE)).gameProfile().getName(), new RegenerationShard().getProfileId())) {
            float rm = Main.getDataHandler().profiles.get(p.getUuid().toString()).getRm();
            if (rm < 2f) {
                rm += 0.1F;
                Main.getDataHandler().profiles.get(p.getUuid().toString()).setRm(rm);
                p.getInventory().removeOne(stack);
                p.sendMessage(Text.literal("§aYou consumed a %s §r§aand gained §c+0.1x §amultiplier!".formatted(new RegenerationShard().getDisplay())), false);
            } else {
                p.sendMessage(Text.literal("§4You have the max §cRegeneration Multiplier §4already!"), false);
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

}
