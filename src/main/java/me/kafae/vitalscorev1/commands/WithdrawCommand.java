package me.kafae.vitalscorev1.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.kafae.vitalscorev1.Main;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Objects;

public class WithdrawCommand {

    // logic
    public int onCommand(CommandContext<ServerCommandSource> ctx) {
        if (ctx.getSource().isExecutedByPlayer()) {
            int amount = IntegerArgumentType.getInteger(ctx, "amount");
            if (amount <= 0) {
                ctx.getSource().sendMessage(Text.literal("§4<amount> cannot be less than 0!"));
                return 0;
            } else {
                float rm = Main.getDataHandler().profiles.get(Objects.requireNonNull(ctx.getSource().getPlayer()).getUuid().toString()).getRm();
                if (amount > (rm*10 - 5)) {
                    ctx.getSource().sendMessage(Text.literal("§4You cannot withdraw that much!"));
                    return 0;
                } else {
                    ctx.getSource().getPlayer().getInventory().offerOrDrop(new RegenerationShard().getItem(amount));
                    ctx.getSource().sendMessage(Text.literal("§aSuccessfully withdrew %s multiplier".formatted(amount)));
                    rm -= (float) amount / 10;
                    Main.getDataHandler().profiles.get(Objects.requireNonNull(ctx.getSource().getPlayer()).getUuid().toString()).setRm(rm);
                    return 1;
                }
            }
        } else {
            ctx.getSource().sendMessage(Text.literal("§4This command can only be executed by a player!"));
            return 0;
        }
    }

}
