package com.example.examplemod

import net.fabricmc.api.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ExampleMod : ModInitializer {

    const val MOD_ID = "examplemod"
    const val MOD_NAME = "ExampleMod"

    @JvmField val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        LOGGER.info("Hello from common side")
    }
}
