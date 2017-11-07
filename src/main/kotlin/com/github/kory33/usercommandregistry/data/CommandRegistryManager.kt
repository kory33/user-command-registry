package com.github.kory33.usercommandregistry.data

import com.github.kory33.usercommandregistry.util.data.PlayerDataFactory
import com.github.kory33.usercommandregistry.util.data.PlayerDataManager
import com.google.gson.JsonElement
import org.bukkit.plugin.java.JavaPlugin

object CommandRegistryFactory: PlayerDataFactory<CommandRegistry> {
    override fun serialize(obj: CommandRegistry) = obj.toJsonArray()
    override fun deserialize(jsonElement: JsonElement) = CommandRegistry(jsonElement.asJsonArray)
    override fun constructEmptyData() = CommandRegistry()
}

class CommandRegistryManager(plugin: JavaPlugin) : PlayerDataManager<CommandRegistry>(plugin, CommandRegistryFactory) {
    override val folderName = "aliases"
}