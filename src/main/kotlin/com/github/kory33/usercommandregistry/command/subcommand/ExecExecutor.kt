package com.github.kory33.usercommandregistry.command.subcommand

import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.ui.AvailableOnlyForPlayerInterface
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ExecExecutor(private val plugin: UserCommandRegistry) : SubCommandExecutor {
    override val helpString = plugin.locale.getString("help.sub_command.exec")

    override fun onCommand(sender: CommandSender, command: Command, args: List<String>): Boolean {
        val player = sender as? Player

        if (player == null) {
            AvailableOnlyForPlayerInterface(plugin.locale).send(sender)
            return true
        }

        TODO("execute alias")
    }
}