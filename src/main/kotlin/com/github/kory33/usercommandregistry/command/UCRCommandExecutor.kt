package com.github.kory33.usercommandregistry.command

import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.command.subcommand.ExecExecutor
import com.github.kory33.usercommandregistry.command.subcommand.HelpExecutor
import com.github.kory33.usercommandregistry.command.subcommand.RegisterExecutor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class UCRCommandExecutor(val plugin : UserCommandRegistry) : CommandExecutor {
    private val executorMap = mutableMapOf(
            Pair("register", RegisterExecutor()),
            Pair("exec", ExecExecutor()),
            Pair("help", HelpExecutor())
    )

    init {
        val aliasMap = mapOf(
                Pair("r", executorMap["register"]!!),
                Pair("e", executorMap["exec"]!!)
        )

        executorMap.putAll(aliasMap)
    }

    private val defaultExecutor = executorMap["help"]!!

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val argList = arrayListOf(*args)

        val executor = if (argList.isEmpty()) {
            defaultExecutor
        } else {
            // match to the subcommand executor
            executorMap[argList.removeAt(0)] ?: defaultExecutor
        }

        val commandStatus = executor.onCommand(sender, command, argList)

        if (!commandStatus) {
            executor.displayHelp(sender)
        }

        return true
    }
}