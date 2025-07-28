package me.kafae.vitalscorev1;

import me.kafae.vitalscorev1.handlers.ConfigHandler;
import me.kafae.vitalscorev1.handlers.CooldownHandler;
import me.kafae.vitalscorev1.handlers.DataHandler;
import me.kafae.vitalscorev1.events.*;
import me.kafae.vitalscorev1.log4j.Log4JLogger;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;
import java.util.UUID;

public class Main implements ModInitializer {

    private static final DataHandler dh = new DataHandler();
    private static final ConfigHandler ch = new ConfigHandler();
    private static final CooldownHandler cdh = new CooldownHandler();
    private static final Log4JLogger logger = new Log4JLogger();

    // other stuff
    public static final UUID regenerationShardUUID = UUID.randomUUID();
    public static final Set<Item> neth = Set.of(
            Items.NETHERITE_HELMET,
            Items.NETHERITE_CHESTPLATE,
            Items.NETHERITE_LEGGINGS,
            Items.NETHERITE_BOOTS,
            Items.NETHERITE_SWORD
    );

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
            Files.createDirectories(getConfigHandler().getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // load config
        getConfigHandler().loadConfig();

        // register events
        new StartServerTickEvent().register();
        new ServerLifeCycleStoppingEvent().register();
        new ServerPlayerJoinEvent().register();
        new ServerPlayerDisconnectEvent().register();
        new ServerLivingEntityDeathEvent().register();
        new UseItemCallbackEvent().register();

        // finish loading
        getLogger().info("Successfully loaded VitalsCoreV1");
    }
}
