package com.github.kory33.usercommandregistry.command.subcommand

import com.github.kory33.usercommandregistry.UserCommandRegistry
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HelpExecutor(private val plugin: UserCommandRegistry,
                   private val subCommandName: String,
                   private val executorMap: MutableMap<String, SubCommandExecutor>): SubCommandExecutor(plugin) {
    override val helpString
        get() = plugin.locale.getAsStringList("help.sub_command.help")

    override fun onCommand(sender: CommandSender, command: Command, args: List<String>): Boolean {
        if (args.isEmpty() || args[0] == subCommandName) {
            return false
        }

        return executorMap[args[0]]?.let {
            it.displayHelp(sender)
            true
        } ?: false
    }
}