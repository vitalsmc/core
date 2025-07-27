package me.kafae.vitalscorev1;

import me.kafae.vitalscorev1.dsh.DataHandler;
import me.kafae.vitalscorev1.events.ServerLivingEntityDeathEvent;
import me.kafae.vitalscorev1.events.ServerPlayerDisconnectEvent;
import me.kafae.vitalscorev1.events.ServerPlayerJoinEvent;
import me.kafae.vitalscorev1.log4j.Log4JLogger;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class Main implements ModInitializer {

    // companion object
    public static final class Companion {
        private static final DataHandler dh = new DataHandler();
        private static final Log4JLogger logger = new Log4JLogger();

        // item uuids
        private static final UUID regenerationShardUUID = UUID.randomUUID();

        // getters
        public static DataHandler getDataHandler() {
            return dh;
        }

        public static UUID getRegenerationShardUUID() {
            return regenerationShardUUID;
        }

        // logger
        public static Logger getLogger() {
            return logger.getLogger();
        }
    }

    @Override
    public void onInitialize() {
        // create files
        try {
            Files.createDirectories(Companion.getDataHandler().getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // register events
        new ServerPlayerJoinEvent().register();
        new ServerPlayerDisconnectEvent().register();
        new ServerLivingEntityDeathEvent().register();

        // finish loading
        Companion.getLogger().info("Successfully loaded VitalsCoreV1");
    }
}
