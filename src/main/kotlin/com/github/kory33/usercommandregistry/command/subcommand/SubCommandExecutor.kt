package com.github.kory33.usercommandregistry.command.subcommand

import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.ui.BrowseableMessageUI
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Abstraction sub-command executor
 * @author Kory
 */
abstract class SubCommandExecutor(private val plugin: UserCommandRegistry) {
    /**
     * Get a string which includes command usage and description
     * @return string which includes command usage and description
     */
    abstract val helpString: List<String>

    /**
     * This method is invoked when a player or console executes a command.
     *
     * @param sender commandSender instance, which has run the command
     * @param command entire command being run
     * @param args list of arguments to be processed (sub-command argument is not included in this list)
     * @return a boolean value, false when the command usage has to be displayed, otherwise true
     */
    abstract fun onCommand(sender: CommandSender, command: Command, args: List<String>): Boolean

    /**
     * Send help message to the target.
     * Without overrides, this method sends the string obtained from [SubCommandExecutor.helpString].
     *
     * @param target target to which the help message is sent
     */
    fun displayHelp(target: CommandSender) {
        if (target !is Player) {
            target.sendMessage(helpString.joinToString("\n"))
            return
        }

        BrowseableMessageUI(target, plugin, this.helpString).send()
    }
}