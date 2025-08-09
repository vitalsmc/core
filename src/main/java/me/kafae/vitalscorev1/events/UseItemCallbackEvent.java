package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.xml.crypto.Data;
import java.util.Objects;

public class UseItemCallbackEvent {

    // register event
    public void register() {
        UseItemCallback.EVENT.register(this::onUseItem);
    }

    // event logic
    private ActionResult onUseItem(PlayerEntity p, World w, Hand h) {
        if (w.isClient()) return ActionResult.PASS;

        ItemStack stack = p.getStackInHand(h);
        Item i = stack.getItem();
        if (stack.isOf(Items.ENDER_PEARL)) {
            if (Main.getCooldownHandler().inCooldown(i, (ServerPlayerEntity) p)) {
                p.sendMessage(Text.literal("§4Item still on cooldown, please wait §e%s!".formatted(Main.getCooldownHandler().getCooldown(i, (ServerPlayerEntity) p))), false);

                p.getInventory().removeStack(p.getInventory().getSelectedSlot());
                p.getInventory().offerOrDrop(stack);

                return ActionResult.FAIL;
            } else {
                Main.getCooldownHandler().setCooldown(i, (ServerPlayerEntity) p);
                return ActionResult.PASS;
            }
        }

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
