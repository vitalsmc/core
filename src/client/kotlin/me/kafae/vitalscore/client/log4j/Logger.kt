package me.kafae.vitalscore.client.log4j

import org.apache.logging.log4j.LogManager


object Logger {

    const val MOD_ID: String = "vitalscore"
    val logger = LogManager.getLogger(MOD_ID)

}