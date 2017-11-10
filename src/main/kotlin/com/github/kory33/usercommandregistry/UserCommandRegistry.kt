package com.github.kory33.usercommandregistry

import com.github.kory33.chatgui.command.RunnableInvoker
import com.github.kory33.chatgui.listener.PlayerChatInterceptor
import com.github.kory33.chatgui.manager.PlayerInteractiveInterfaceManager
import com.github.kory33.updatenotificationplugin.bukkit.github.GithubUpdateNotifyPlugin
import com.github.kory33.usercommandregistry.command.UCRCommandExecutor
import com.github.kory33.usercommandregistry.data.CommandRegistry
import com.github.kory33.usercommandregistry.data.CommandRegistryManager
import com.github.kory33.usercommandregistry.util.config.LocaleConfig
import com.github.kory33.usercommandregistry.util.data.PlayerDataAutoSaver
import org.bstats.bukkit.Metrics
import org.bukkit.event.HandlerList
import java.io.File

const val COMMAND_STRING = "ucr"

class UserCommandRegistry : GithubUpdateNotifyPlugin() {
    var chatInterceptor: PlayerChatInterceptor? = null
        private set
    var interfaceManager : PlayerInteractiveInterfaceManager? = null
        private set
    var runnableInvoker: RunnableInvoker? = null
        private set
    private var metrics: Metrics? = null

    var commandRegistryManager: CommandRegistryManager? = null
        private set

    private var autoSaver: PlayerDataAutoSaver<CommandRegistry>? = null

    lateinit var locale: LocaleConfig
        private set

    private fun getMetricsInstance() : Metrics {
        val metrics = Metrics(this)

        return metrics
    }

    private fun getLocaleConfiguration(): LocaleConfig {
        val localeConfigFile = File(this.dataFolder, "locale.json")
        if (!localeConfigFile.exists()) {
            this.saveResource("locale.json", false)
        }

        return LocaleConfig(localeConfigFile)
    }

    override fun getGithubRepository() = "kory33/user-command-registry"

    override fun onEnable() {
        locale = getLocaleConfiguration()

        runnableInvoker = runnableInvoker ?: RunnableInvoker.getRegisteredInstance(this, COMMAND_STRING)
        interfaceManager = interfaceManager ?: PlayerInteractiveInterfaceManager()
        commandRegistryManager = commandRegistryManager ?: CommandRegistryManager(this)
        chatInterceptor = chatInterceptor ?: PlayerChatInterceptor(this)

        autoSaver = PlayerDataAutoSaver(commandRegistryManager!!, 20 * 60 * 5, false)

        getCommand(COMMAND_STRING).executor = UCRCommandExecutor(this)

        metrics = metrics ?: getMetricsInstance()
    }

    override fun onDisable() {
        autoSaver?.stopAutoSaveTask()
        autoSaver = null

        commandRegistryManager?.saveAllPlayerDataSync()

        HandlerList.unregisterAll(this)
    }

    /**
     * Reload the plugin
     */
    fun reload() {
        this.onDisable()
        this.onEnable()
    }
}