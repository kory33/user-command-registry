package com.github.kory33.usercommandregistry.util.data

import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DataAutoSaver(private val dataManager: IWritable,
                    private val taskIssuerPlugin: JavaPlugin,
                    private val autoSaveIntervalTicks: Long,
                    private val shouldLog: Boolean = true) {
    private var nextAutoSaveTaskId: Int? = null

    private val taskScheduler = this.taskIssuerPlugin.server.scheduler
    private val saveTaskExecutor: ExecutorService = Executors.newFixedThreadPool(1)

    private fun scheduleNextAutoSaveTask() = taskScheduler.scheduleSyncDelayedTask(
            this.taskIssuerPlugin,
            this::save,
            this.autoSaveIntervalTicks
    )

    private fun save() {
        if (this.shouldLog) {
            this.taskIssuerPlugin.logger.info("Data is being saved asynchronously...")
        }

        CompletableFuture.runAsync(Runnable { dataManager.writeData() }, this.saveTaskExecutor)

        nextAutoSaveTaskId = scheduleNextAutoSaveTask()
    }

    /**
     * Stop and cancel the autosave task.
     */
    fun pauseAutoSaveTask() {
        nextAutoSaveTaskId?.let { taskScheduler.cancelTask(it) }
        nextAutoSaveTaskId = null
    }

    /**
     * Resumes the auto-save task if it has been paused
     */
    fun resumeAutoSaveTask() {
        nextAutoSaveTaskId = nextAutoSaveTaskId ?: scheduleNextAutoSaveTask()
    }

    init {
        this.scheduleNextAutoSaveTask()
    }
}