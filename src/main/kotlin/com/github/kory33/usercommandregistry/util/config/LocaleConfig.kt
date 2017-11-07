package com.github.kory33.usercommandregistry.util.config

import com.github.kory33.usercommandregistry.util.FileUtil
import org.bukkit.Bukkit
import java.io.File
import java.text.MessageFormat
import java.util.logging.Level

/**
 * Class which handles locale strings loaded from the specified file.
 *
 * @param configFile File from which json object is read.
 */
class LocaleConfig(configFile: File) {
    private val configJsonObject = FileUtil.readJson(configFile).asJsonObject

    private fun fetchStringElement(chainedKey: String, delimiter: String = "."): String? {
        val targetElement = chainedKey
                .split(delimiter)
                .foldRight(configJsonObject, { key, obj -> obj?.getAsJsonObject(key) })

        return targetElement?.asString
    }

    /**
     * Get the string data with the specified json key.
     * @param jsonKey key to the string value, joint with "."(period)
     *
     * @return String if found at the given key location,
     * otherwise the key itself(warning will be logged in this case)
     */
    fun getString(jsonKey: String): String {
        val result = fetchStringElement(jsonKey)
        if (result != null) {
            return result
        }

        Bukkit.getLogger().log(Level.WARNING, "Failed to fetch the massage at $jsonKey. Using this key instead.")
        return jsonKey
    }

    /**
     * Get a message of specified jsonKey formatted using an object
     * @param jsonKey key indicating the format template string field
     * *
     * @return string at the location of key, formatted with several objects
     */
    fun getFormatted(jsonKey: String, vararg arguments: Any): String {
        return MessageFormat.format(this.getString(jsonKey), *arguments)
    }
}