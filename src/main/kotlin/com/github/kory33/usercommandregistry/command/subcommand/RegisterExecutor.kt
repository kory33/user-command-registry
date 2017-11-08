package com.github.kory33.usercommandregistry.command.subcommand

import com.github.kory33.usercommandregistry.UCRPermissions
import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.data.CommandAlias
import com.github.kory33.usercommandregistry.ui.SimpleMessageUI
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RegisterExecutor(private val plugin: UserCommandRegistry) : SubCommandExecutor {
    override val helpString = plugin.locale.getString("help.sub_command.register")

    override fun onCommand(sender: CommandSender, command: Command, args: List<String>): Boolean {
        val locale = plugin.locale
        val player = sender as? Player

        if (player == null) {
            SimpleMessageUI(locale.getString("ui.message.player_only"), locale).send(sender)
            return true
        }

        if (!player.hasPermission(UCRPermissions.REGISTER)) {
            SimpleMessageUI(plugin.locale.getString("permission.missing"), plugin.locale)
            return true
        }

        if (args.size < 2) {
            return false
        }

        val registry = plugin.commandRegistryManager!!.getLoadedPlayerData(player.uniqueId)
        if (registry == null) {
            SimpleMessageUI(locale.getString("ui.message.data_loading"), locale).send(player)
            return true
        }

        val alias = CommandAlias(args[0], args.drop(1).joinToString(separator = " "))

        registry.addAlias(alias)

        val completionMessage
                = locale.getFormatted("ui.message.register.registered", alias.aliasString, alias.targetCommand)
        SimpleMessageUI(completionMessage, locale)

        return true
    }
}