package com.github.kory33.usercommandregistry.json

import com.google.gson.JsonElement

interface PlayerDataFactory<T> {
    /**
     * Serialize an object with type T into a JsonElement object.
     */
    fun serialize(obj: T): JsonElement

    /**
     * Deserialize a JsonElement object and return an object with type T.
     */
    fun deserialize(jsonElement: JsonElement): T

    /**
     * Construct an empty player data.
     */
    fun constructEmptyData(): T
}