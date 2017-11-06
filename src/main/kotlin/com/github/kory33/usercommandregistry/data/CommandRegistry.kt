package com.github.kory33.usercommandregistry.data

import com.google.gson.JsonArray

class CommandRegistry() {
    private val aliasSet: MutableSet<CommandAlias> = HashSet()

    constructor(array: JsonArray) : this() {
        array.map {
            try { CommandAlias(it.asJsonObject) } catch (_ : Exception) { null }
        }.forEach {
            addAlias(it ?: return@forEach)
        }
    }

    fun addAlias(alias: CommandAlias) {
        val existingAlias = getAlias(alias.aliasString)

        aliasSet.remove(existingAlias)
        aliasSet.add(alias)
    }

    fun getAlias(alias: String) = aliasSet.firstOrNull { it.aliasString == alias }

    fun toJsonArray(): JsonArray {
        val array = JsonArray()

        aliasSet.forEach { array.add(it.toJsonObject()) }

        return array
    }
}