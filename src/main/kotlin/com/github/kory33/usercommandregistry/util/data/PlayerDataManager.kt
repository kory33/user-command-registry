package com.github.kory33.usercommandregistry.util.data

import com.github.kory33.usercommandregistry.util.readAsJson
import com.github.kory33.usercommandregistry.util.writeJson
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.IOException
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.function.Supplier

/**
 * Class that manages player data instances associated with logged-in players.
 *
 * As it loads player's data asynchronously,
 * it is possible that the data has not been loaded for a few ticks after the player join.
 *
 * @param T type of the player-related data
 * @param plugin plugin from which data is read
 * @param folderName The name of the folder which the data is read from or written to
 * @param factory factory class to read and write json to the data folder
 */
abstract class PlayerDataManager<out T> protected constructor(internal val plugin: JavaPlugin,
                                                              private val folderName: String,
                                                              private val factory: PlayerDataFactory<T>) : Listener {

    private val fileRWExecutor = Executors.newFixedThreadPool(1)
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

        val serializedData = factory.serialize(playerData)
        getPlayerDataTargetFile(playerUuid).writeJson(serializedData)
    }

    private fun loadPlayerDataSync(playerUuid: UUID) = getPlayerDataTargetFile(playerUuid).readAsJson()

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

        CompletableFuture.supplyAsync(Supplier {
            val loadedData = loadPlayerDataSync(playerUuid)
            factory.deserialize(loadedData)
        }, fileRWExecutor).exceptionally {
            factory.constructEmptyData()
        }.thenAccept {
            playerDataMap.put(playerUuid, it)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        CompletableFuture.runAsync(Runnable {
            savePlayerDataSync(event.player.uniqueId)
        }, fileRWExecutor).thenRun {
            playerDataMap.remove(event.player.uniqueId)
        }
    }
}