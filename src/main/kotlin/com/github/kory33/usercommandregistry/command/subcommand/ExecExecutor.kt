package com.github.kory33.usercommandregistry.command.subcommand

import com.github.kory33.usercommandregistry.UCRPermissions
import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.ui.SimpleMessageUI
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ExecExecutor(private val plugin: UserCommandRegistry) : SubCommandExecutor {
    /**
     * The server tends to crash itself in the following situation:
     *  - There is an alias of `crash` -> `ucr exec crash`
     *  - Player executes `ucr exec crash`
     *
     * In which case, this executor needs to know if the command has been executed within the same command execution.
     * Therefore, it first checks if the player has already executed the command,
     * if not, put the alias into this set and then execute the alias, and then empty the execution stack.
     *
     * This kind of mechanism is only valid in a single threaded execution, therefore it fails in any environment
     * which makes use of multithreaded command executor.
     */
    private val executingAlias = HashSet<String>()

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

        val registry = plugin.commandRegistryManager?.getLoadedPlayerData(player.uniqueId)
        if (registry == null) {
            SimpleMessageUI(locale["ui.message.data_loading"], locale).send(player)
            return true
        }

        val alias = args.first()
        val aliasTarget = registry.getAlias(alias)
        if (aliasTarget == null) {
            SimpleMessageUI(locale["ui.message.exec.target_missing"], locale).send(player)
            return true
        }

        if (executingAlias.contains(alias)) {
            SimpleMessageUI(locale.getFormatted("ui.message.exec.recursion_detected", alias), locale).send(player)
            return true
        }

        executingAlias.add(alias)

        player.sendMessage(locale.getFormatted("ui.message.exec.performing", aliasTarget.targetCommand))
        player.performCommand(aliasTarget.targetCommand)

        executingAlias.remove(alias)
        return true
    }
}