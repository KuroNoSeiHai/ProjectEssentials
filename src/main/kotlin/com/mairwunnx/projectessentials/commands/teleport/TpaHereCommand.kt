package com.mairwunnx.projectessentials.commands.teleport

import com.mairwunnx.projectessentials.ProjectEssentials
import com.mairwunnx.projectessentials.commands.CommandBase
import com.mairwunnx.projectessentials.configurations.ModConfiguration.getCommandsConfig
import com.mairwunnx.projectessentials.core.helpers.throwOnlyPlayerCan
import com.mairwunnx.projectessentials.core.helpers.throwPermissionLevel
import com.mairwunnx.projectessentials.extensions.sendMsg
import com.mairwunnx.projectessentials.permissions.permissions.PermissionsAPI
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder.literal
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.command.Commands
import net.minecraft.command.arguments.EntityArgument
import org.apache.logging.log4j.LogManager

object TpaHereCommand : CommandBase() {
    private val logger = LogManager.getLogger()
    private var config = getCommandsConfig().commands.tpaHere

    init {
        command = "tpahere"
        aliases = config.aliases.toMutableList()
    }

    override fun reload() {
        config = getCommandsConfig().commands.tpaHere
        aliases = config.aliases.toMutableList()
        super.reload()
    }

    override fun register(dispatcher: CommandDispatcher<CommandSource>) {
        super.register(dispatcher)
        aliases.forEach { command ->
            dispatcher.register(literal<CommandSource>(command)
                .then(
                    Commands.argument("player", EntityArgument.player()).executes {
                        return@executes execute(it, true)
                    }
                )
            )
        }
    }

    override fun execute(
        c: CommandContext<CommandSource>,
        argument: Any?
    ): Int {
        super.execute(c, argument)

        if (senderIsServer) {
            throwOnlyPlayerCan(command)
            return 0
        } else {
            if (PermissionsAPI.hasPermission(senderName, "ess.tpahere")) {
                if (ProjectEssentials.teleportPresenter.commitRequestHere(
                        senderPlayer.name.string,
                        targetPlayer.name.string
                    )
                ) {
                    sendMsg(sender, "tpahere.success", targetName)
                    sendMsg(target, "tpahere.recipient_success", senderName)
                } else {
                    sendMsg(sender, "tpahere.request_exist", targetName)
                    sendMsg(target, "tpahere.tried_to_teleport", senderName)
                }
            } else {
                throwPermissionLevel(senderName, command)
                sendMsg(sender, "tpahere.restricted")
                return 0
            }
        }
        logger.info("Executed command \"/${command}\" from $senderName")
        return 0
    }
}
