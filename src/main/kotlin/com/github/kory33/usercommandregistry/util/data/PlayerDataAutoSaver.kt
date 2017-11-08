package com.github.kory33.usercommandregistry.util.data

import org.bukkit.Server
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PlayerDataAutoSaver<T>(private val dataManager: PlayerDataManager<T>,
                             private val autoSaveIntervalTicks: Long,
                             private val shouldLog: Boolean = true) {

    private var nextAutoSaveTaskId: Int = 0

    private val plugin = dataManager.plugin
    private val server: Server = plugin.server
    private val saveTaskExecutor: ExecutorService = Executors.newFixedThreadPool(1)

    private fun scheduleNextAutoSaveTask() {
        CompletableFuture.runAsync(Runnable { dataManager.saveAllPlayerDataSync() }, this.saveTaskExecutor)

        if (this.shouldLog) {
            this.plugin.logger.info("Session data is being saved asynchronously...")
        }

        this.nextAutoSaveTaskId = server.scheduler.scheduleSyncDelayedTask(
                this.plugin,
                { this.scheduleNextAutoSaveTask() },
                this.autoSaveIntervalTicks
        )
    }

    /**
     * Stop and cancel the autosave task.
     */
    fun stopAutoSaveTask() {
        server.scheduler.cancelTask(this.nextAutoSaveTaskId)
    }

    init {
        this.scheduleNextAutoSaveTask()
    }
}