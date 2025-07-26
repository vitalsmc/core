package me.kafae.vitalscore.client

import me.kafae.vitalscore.log4j.Logger
import net.fabricmc.api.ClientModInitializer

class MainClient : ClientModInitializer {

    companion object {
        val logger = Logger.logger
    }

    override fun onInitializeClient() {
        // log fully initialization message
        logger.info("Successfully loaded Vital's Core Module (Client)")
    }
}
