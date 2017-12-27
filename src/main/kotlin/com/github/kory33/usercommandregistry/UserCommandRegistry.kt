package com.github.kory33.usercommandregistry

import com.github.kory33.chatgui.command.RunnableInvoker
import com.github.kory33.chatgui.listener.PlayerChatInterceptor
import com.github.kory33.chatgui.manager.PlayerInteractiveInterfaceManager
import com.github.kory33.updatenotificationplugin.bukkit.github.GithubUpdateNotifyPlugin
import com.github.kory33.usercommandregistry.command.UCRCommandExecutor
import com.github.kory33.usercommandregistry.data.CommandRegistryManager
import com.github.kory33.usercommandregistry.util.config.LocaleConfig
import com.github.kory33.usercommandregistry.util.data.DataAutoSaver
import org.bstats.bukkit.Metrics
import org.bukkit.event.HandlerList
import java.io.File

const val COMMAND_STRING = "ucr"

class UserCommandRegistry : GithubUpdateNotifyPlugin() {
    lateinit var chatInterceptor: PlayerChatInterceptor
        private set
    lateinit var interfaceManager : PlayerInteractiveInterfaceManager
        private set
    lateinit var runnableInvoker: RunnableInvoker
        private set

    private var metrics: Metrics? = null

    lateinit var commandRegistryManager: CommandRegistryManager
        private set

    private var areLateinitInitialized = false

    private var autoSaver: DataAutoSaver? = null

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

        if (!areLateinitInitialized) {
            runnableInvoker = RunnableInvoker.getRegisteredInstance(this, COMMAND_STRING)!!
            interfaceManager = PlayerInteractiveInterfaceManager()
            chatInterceptor = PlayerChatInterceptor(this)

            commandRegistryManager = CommandRegistryManager(this)

            areLateinitInitialized = true
        }

        autoSaver = DataAutoSaver(commandRegistryManager, this, 20 * 60 * 5, false)

        getCommand(COMMAND_STRING).executor = UCRCommandExecutor(this)

        metrics = metrics ?: getMetricsInstance()
    }

    override fun onDisable() {
        if (!this.isEnabled) return

        autoSaver?.pauseAutoSaveTask()
        autoSaver = null

        commandRegistryManager.writeData()

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