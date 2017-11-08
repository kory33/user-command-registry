package com.github.kory33.usercommandregistry.util.data

import com.github.kory33.usercommandregistry.util.FileUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Class that manages player data instances associated with logged-in players.
 *
 * As it loads player's data asynchronously,
 * it is possible that the data has not been loaded for a few ticks after the player join.
 *
 * @param T type of the player-related data
 */
abstract class PlayerDataManager<out T> protected constructor(plugin: JavaPlugin,
                                                              private val factory: PlayerDataFactory<T>) : Listener {
    /**
     * The name of the folder which the data is read from or written to.
     * The implementation of this field should be a constant.
     */
    protected abstract val folderName: String

    private val saveTargetDirectory = plugin.dataFolder.resolve(folderName)
    private val playerDataMap = HashMap<UUID, T>()

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)

        if (!saveTargetDirectory.isDirectory && !saveTargetDirectory.mkdir()) {
            throw IOException("Failed to initialize data directory : " + saveTargetDirectory.absolutePath)
        }
    }

    private fun getPlayerDataTargetFile(playerUuid: UUID) = saveTargetDirectory.resolve(playerUuid.toString() + ".json")

    private fun savePlayerDataSync(playerUuid: UUID) {
        val playerData = playerDataMap[playerUuid] ?: return
        val dataJson = factory.serialize(playerData)
        FileUtil.writeJson(getPlayerDataTargetFile(playerUuid), dataJson)
    }

    private fun loadPlayerDataSync(playerUuid: UUID) = FileUtil.readJson(getPlayerDataTargetFile(playerUuid))

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
            factory.deserialize(loadPlayerDataSync(playerUuid))
        }.exceptionally {
            factory.constructEmptyData()
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