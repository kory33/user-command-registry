package com.github.kory33.usercommandregistry.command

import com.github.kory33.usercommandregistry.UserCommandRegistry
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class UCRCommandExecutor(val plugin : UserCommandRegistry) : CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        // TODO
        return true
    }
}