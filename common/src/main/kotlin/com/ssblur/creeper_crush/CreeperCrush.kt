package com.ssblur.creeper_crush

import com.ssblur.unfocused.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object CreeperCrush : ModInitializer("creeper_crush") {
    const val MODID = "creeper_crush"
    val LOGGER: Logger = LoggerFactory.getLogger(id)

    fun init() {
        LOGGER.info("Creeper Crush loaded...")
    }

    fun clientInit() {
        LOGGER.info("Creeper Crush loaded...")
    }
}