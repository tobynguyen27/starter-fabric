package com.example.examplemod

import net.fabricmc.api.ClientModInitializer

object ExampleModClient : ClientModInitializer {
    override fun onInitializeClient() {
        ExampleMod.LOGGER.info("Hello from client side")
    }
}
