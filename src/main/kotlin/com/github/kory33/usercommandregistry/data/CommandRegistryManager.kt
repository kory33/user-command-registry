package com.github.kory33.usercommandregistry.data

import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.util.FileUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.io.IOException
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Class that manages CommandRegistry instances associated with logged-in players.
 * As it loads player's data asynchronously,
 * it is possible that the data has not been loaded for a few ticks after the player join.
 */
class CommandRegistryManager(plugin: UserCommandRegistry) : Listener {
    private val playerDataMap = HashMap<UUID, CommandRegistry>()
    private val saveTargetDirectory = plugin.dataFolder.resolve("aliases")

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)

        if (!saveTargetDirectory.isDirectory && !saveTargetDirectory.mkdir()) {
            throw IOException("Failed to initialize data directory : " + saveTargetDirectory.absolutePath)
        }
    }

    private fun getPlayerDataTargetFile(playerUuid: UUID) = saveTargetDirectory.resolve(playerUuid.toString() + ".json")

    private fun savePlayerDataSync(playerUuid: UUID) {
        val commandRegistry = playerDataMap[playerUuid] ?: return
        val dataJson = commandRegistry.toJsonArray()

        FileUtil.writeJson(getPlayerDataTargetFile(playerUuid), dataJson)
    }

    /**
     * Get the player data which has been loaded.
     *
     * As this class loads player's data asynchronously,
     * it is possible that the data has not been loaded for a few ticks after the player join.
     *
     * In which case, this method returns `null`.
     *
     * @param playerUuid uuid of the player
     */
    fun getLoadedPlayerData(playerUuid: UUID) = playerDataMap[playerUuid]

    /**
     * Save all the loaded data synchronously.
     */
    fun saveAllPlayerDataSync() = playerDataMap.keys.forEach { savePlayerDataSync(it) }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val playerUuid = event.player.uniqueId

        CompletableFuture.supplyAsync {
            CommandRegistry(FileUtil.readJson(getPlayerDataTargetFile(playerUuid)).asJsonArray)
        }.exceptionally {
            CommandRegistry()
        }.thenAccept {
            playerDataMap.put(playerUuid, it)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        CompletableFuture.runAsync {
            savePlayerDataSync(event.player.uniqueId)
        }.thenRun {
            playerDataMap.remove(event.player.uniqueId)
        }
    }
}