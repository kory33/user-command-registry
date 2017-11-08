package com.github.kory33.usercommandregistry.command.subcommand

import com.github.kory33.usercommandregistry.UserCommandRegistry
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HelpExecutor(private val plugin: UserCommandRegistry) : SubCommandExecutor {
    override val helpString
            get() = plugin.locale.getString("help.sub_command.help")

    override fun onCommand(sender: CommandSender, command: Command, args: List<String>): Boolean {
        return false
    }
}