package com.github.kory33.usercommandregistry.command.subcommand

import com.github.kory33.usercommandregistry.UCRPermissions
import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.ui.SimpleMessageUI
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReloadExecutor(private val plugin: UserCommandRegistry) : SubCommandExecutor(plugin) {
    override val helpString
        get() = plugin.locale.getAsStringList("help.sub_command.reload")

    override fun onCommand(sender: CommandSender, command: Command, args: List<String>): Boolean {
        val player = sender as? Player
        val hasEnoughPermission = player?.hasPermission(UCRPermissions.RELOAD) ?: true

        if (!hasEnoughPermission) {
            SimpleMessageUI(plugin.locale["permission.missing"], plugin.locale).send(sender)
            return true
        }

        sender.sendMessage(plugin.locale["ui.message.reload.start"])
        plugin.reload()
        sender.sendMessage(plugin.locale["ui.message.reload.complete"])

        return true
    }
}