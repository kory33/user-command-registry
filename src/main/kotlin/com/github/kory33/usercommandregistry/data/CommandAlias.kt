package com.github.kory33.usercommandregistry.data

import com.google.gson.JsonObject

class CommandAlias(val aliasString: String, val targetCommand: String) {
    fun toJsonObject(): JsonObject {
        val obj = JsonObject()

        obj.addProperty("alias", aliasString)
        obj.addProperty("target", targetCommand)

        return obj
    }

    companion object {
        fun fromJsonObject(obj: JsonObject) = CommandAlias(obj["alias"].asString, obj["target"].asString)
    }
}