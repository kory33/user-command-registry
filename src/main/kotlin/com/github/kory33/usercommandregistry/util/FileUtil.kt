package com.github.kory33.usercommandregistry.util

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

private val fileFormat = Charset.forName("utf-8")

/**
 * Write json data to the file
 * @param jsonObject a source json object
 */
fun File.writeJson(jsonObject: JsonElement) {
    if (!this.exists()) {
        val parent = this.parentFile
        if (!parent.exists() && !parent.mkdirs()) {
            throw IOException("Failed to create target directory!")
        }
    }

    Files.newOutputStream(this.toPath()).use { oStream ->
        val writeData = jsonObject.toString().toByteArray(fileFormat)
        oStream.write(writeData)
    }
}

/**
 * Read json data from the file
 */
fun File.readAsJson(): JsonElement {
    Files.newBufferedReader(this.toPath(), fileFormat)
            .use { reader -> return JsonParser().parse(reader) }
}