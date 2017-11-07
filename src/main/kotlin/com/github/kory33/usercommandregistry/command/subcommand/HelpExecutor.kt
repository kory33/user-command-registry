package com.github.kory33.usercommandregistry.command.subcommand

import com.github.kory33.usercommandregistry.UserCommandRegistry
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HelpExecutor(plugin: UserCommandRegistry) : SubCommandExecutor {
    override val helpString = plugin.locale.getString("help.sub_command.help")

    override fun onCommand(sender: CommandSender, command: Command, args: List<String>): Boolean {
        return false
    }
}