package me.kafae.vitalscorev1.events;

import me.kafae.vitalscorev1.Main;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

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
                return ActionResult.FAIL;
            } else {
                Main.getCooldownHandler().setCooldown(i, (ServerPlayerEntity) p);
                return ActionResult.PASS;
            }
        }
        return ActionResult.PASS;
    }

}
