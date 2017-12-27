package com.github.kory33.usercommandregistry.util.data

import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DataAutoSaver(private val dataManager: IWritable,
                    private val taskIssuerPlugin: JavaPlugin,
                    private val autoSaveIntervalTicks: Long,
                    private val shouldLog: Boolean = true) {

    private var nextAutoSaveTaskId: Int = 0

    private val taskScheduler = this.taskIssuerPlugin.server.scheduler
    private val saveTaskExecutor: ExecutorService = Executors.newFixedThreadPool(1)

    private fun scheduleNextAutoSaveTask() {
        CompletableFuture.runAsync(Runnable { dataManager.writeData() }, this.saveTaskExecutor)

        if (this.shouldLog) {
            this.taskIssuerPlugin.logger.info("Data is being saved asynchronously...")
        }

        this.nextAutoSaveTaskId = taskScheduler.scheduleSyncDelayedTask(
                this.taskIssuerPlugin,
                this::scheduleNextAutoSaveTask,
                this.autoSaveIntervalTicks
        )
    }

    /**
     * Stop and cancel the autosave task.
     */
    fun stopAutoSaveTask() {
        taskScheduler.cancelTask(this.nextAutoSaveTaskId)
    }

    init {
        this.scheduleNextAutoSaveTask()
    }
}