package com.github.kory33.usercommandregistry

import com.github.kory33.chatgui.command.RunnableInvoker
import com.github.kory33.chatgui.listener.PlayerChatInterceptor
import com.github.kory33.chatgui.manager.PlayerInteractiveInterfaceManager
import com.github.kory33.updatenotificationplugin.bukkit.github.GithubUpdateNotifyPlugin
import com.github.kory33.usercommandregistry.command.UCRCommandExecutor
import org.bstats.bukkit.Metrics
import org.bukkit.event.HandlerList

const val COMMAND_STRING = "ucr"

class UserCommandRegistry : GithubUpdateNotifyPlugin() {
    var chatInterceptor: PlayerChatInterceptor? = null
        private set

    var interfaceManager : PlayerInteractiveInterfaceManager? = null
        private set

    var runnableInvoker: RunnableInvoker? = null
        private set

    override fun getGithubRepository() = "kory33/user-command-registry"

    private fun enableMetrics() {
        var metrics = Metrics(this)
    }

    override fun onEnable() {
        // setup runnable invoker
        if (this.runnableInvoker == null) {
            this.runnableInvoker = RunnableInvoker.getRegisteredInstance(this, COMMAND_STRING)
        }

        // setup player interface manager
        if (this.interfaceManager == null) {
            this.interfaceManager = PlayerInteractiveInterfaceManager()
        }

        getCommand(COMMAND_STRING).executor = UCRCommandExecutor(this)

        this.chatInterceptor = PlayerChatInterceptor(this)

        enableMetrics()
    }

    override fun onDisable() {
        if (!this.isEnabled) {
            return
        }

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