package me.kafae.vitalscore

import me.kafae.vitalscore.dsh.DataStoreHandler
import me.kafae.vitalscore.events.*
import me.kafae.vitalscore.log4j.Logger
import net.fabricmc.api.ModInitializer
import java.nio.file.Files

class Main : ModInitializer {

    companion object {
        val dsh: DataStoreHandler = DataStoreHandler()
        val profiles: MutableMap<String, DataStoreHandler.Profile> = mutableMapOf()
        val logger = Logger.logger
    }

    override fun onInitialize() {
        // check if necessary files are created
        Files.createDirectories(dsh.dir)

        // register events
        PlayerJoinHandler.register()
        PlayerQuitHandler.register()
        LivingEntityDeath.register()

        // log fully initialization message
        logger.info("Successfully loaded Vital's Core Module (Server)")
    }
}
