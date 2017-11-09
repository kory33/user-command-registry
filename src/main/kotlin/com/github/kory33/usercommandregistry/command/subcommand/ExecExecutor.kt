package com.github.kory33.usercommandregistry.command.subcommand

import com.github.kory33.usercommandregistry.UCRPermissions
import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.ui.SimpleMessageUI
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ExecExecutor(private val plugin: UserCommandRegistry) : SubCommandExecutor {
    override val helpString
            get() = plugin.locale["help.sub_command.exec"]

    override fun onCommand(sender: CommandSender, command: Command, args: List<String>): Boolean {
        val locale = plugin.locale
        val player = sender as? Player

        if (player == null) {
            SimpleMessageUI(locale["ui.message.player_only"], locale).send(sender)
            return true
        }

        if (!player.hasPermission(UCRPermissions.EXEC)) {
            SimpleMessageUI(plugin.locale["permission.missing"], plugin.locale).send(player)
            return true
        }

        if (args.isEmpty()) {
            return false
        }

        val registry = plugin.commandRegistryManager!!.getLoadedPlayerData(player.uniqueId)
        if (registry == null) {
            SimpleMessageUI(locale["ui.message.data_loading"], locale).send(player)
            return true
        }

        val aliasTarget = registry.getAlias(args.first())
        if (aliasTarget == null) {
            SimpleMessageUI(locale["ui.message.exec.target_missing"], locale).send(player)
            return true
        }

        player.sendMessage(locale.getFormatted("ui.message.exec.performing", aliasTarget.targetCommand))
        player.performCommand(aliasTarget.targetCommand)
        return true
    }
}