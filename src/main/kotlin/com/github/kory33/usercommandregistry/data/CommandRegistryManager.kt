package com.github.kory33.usercommandregistry.data

import com.github.kory33.usercommandregistry.UserCommandRegistry
import com.github.kory33.usercommandregistry.json.PlayerDataFactory
import com.google.gson.JsonElement

object CommandRegistryFactory: PlayerDataFactory<CommandRegistry> {
    override fun serialize(obj: CommandRegistry) = obj.toJsonArray()
    override fun deserialize(jsonElement: JsonElement) = CommandRegistry(jsonElement.asJsonArray)
    override fun constructEmptyData() = CommandRegistry()
}

class CommandRegistryManager(plugin: UserCommandRegistry)
    : PlayerDataManager<CommandRegistry>(plugin, CommandRegistryFactory) {

    override val folderName = "aliases"
}