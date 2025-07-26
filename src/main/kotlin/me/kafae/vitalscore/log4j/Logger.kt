package me.kafae.vitalscore.log4j

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


object Logger {

    const val MOD_ID: String = "vitalscore"
    val logger: Logger = LogManager.getLogger(MOD_ID)

}