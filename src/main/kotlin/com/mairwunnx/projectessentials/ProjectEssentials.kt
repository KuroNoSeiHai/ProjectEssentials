@file:Suppress("unused")

package com.mairwunnx.projectessentials

import com.mairwunnx.projectessentials.commands.CommandsBase
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

const val MOD_ID = "project_essentials"
const val MOD_NAME = "Project Essentials"
const val MOD_VERSION = "1.14.4-0.0.2.0"

@Mod(MOD_ID)
class ProjectEssentials {
    private val logger: Logger = LogManager.getLogger()

    init {
        logger.info("$MOD_NAME $MOD_VERSION starting initializing ...")
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onServerStarting(it: FMLServerStartingEvent) {
        logger.info("$MOD_NAME $MOD_VERSION starting mod loading ...")
        val commandsBase = CommandsBase()
        commandsBase.registerAll(it.server.commandManager.dispatcher)
    }
}
