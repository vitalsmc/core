package me.kafae.vitalscorev1;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.kafae.vitalscorev1.TaskScheduler.TaskScheduler;
import me.kafae.vitalscorev1.commands.WithdrawCommand;
import me.kafae.vitalscorev1.events.*;
import me.kafae.vitalscorev1.handlers.ConfigHandler;
import me.kafae.vitalscorev1.handlers.CooldownHandler;
import me.kafae.vitalscorev1.handlers.DataHandler;
import me.kafae.vitalscorev1.items.VitalsItem;
import me.kafae.vitalscorev1.items.head.RegenerationShard;
import me.kafae.vitalscorev1.log4j.Log4JLogger;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Main implements ModInitializer {

    private static final DataHandler dh = new DataHandler();
    private static final ConfigHandler ch = new ConfigHandler();
    private static final CooldownHandler cdh = new CooldownHandler();
    private static final Log4JLogger logger = new Log4JLogger();

    // other stuff
    public static final Set<Item> neth = Set.of(
            Items.NETHERITE_HELMET,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_BOOTS,
            Items.NETHERITE_SWORD
    );

    public static final List<Item> bannedEChestItems = new ArrayList<>() {{
        add(Items.MACE);
    }};

    public static final Map<String, VitalsItem> itemStringMap = new HashMap<>() {{
        put("regeneration_shard", new RegenerationShard());
    }};

    public static final List<String> itemsIdList = new ArrayList<>() {{
        this.addAll(itemStringMap.keySet());
    }};

    public static MinecraftServer server;

    public static boolean allowEndEscape = true;

    // getters
    public static DataHandler getDataHandler() {
        return dh;
    }

    public static ConfigHandler getConfigHandler() {
        return ch;
    }

    public static CooldownHandler getCooldownHandler() {
        return cdh;
    }

    public static Log4JLogger getLogger() {
        return logger;
    }

    @Override
    public void onInitialize() {
        // create files
        try {
            Files.createDirectories(getDataHandler().getPath());
            Files.createDirectories(getDataHandler().getBackupPath());
            Files.createDirectories(getConfigHandler().getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // load config
        getConfigHandler().loadConfig();

        // register commands
        CommandRegistrationCallback.EVENT.register((d, r, e) -> {
            // withdraw command
            d.register(
                CommandManager.literal("withdraw")
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(1))
                                .executes(ctx -> new WithdrawCommand().onCommand(ctx))
                        ));
                });



        // register events
        new StartServerTickEvent().register();
        new ServerLifeCycleStoppingEvent().register();
        new ServerPlayerJoinEvent().register();
        new ServerPlayerDisconnectEvent().register();
        new ServerLivingEntityDeathEvent().register();
        new ServerEntityLoadEvent().register();
        new UseItemCallbackEvent().register();
        new UseBlockCallbackEvent().register();

        // register task scheduler
        TaskScheduler.register();

        // finish loading
        getLogger().info("Successfully loaded VitalsCoreV1");
    }
}
