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
 * An util class which handles file I/O
 */
object FileUtil {
    /**
     * delete folder and its content recursively.
     * @param targetDirectory directory to be deleted
     */
    fun deleteFolderRecursively(targetDirectory: File) {
        try {
            Files.walkFileTree(targetDirectory.toPath(), object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    Files.delete(file)
                    return FileVisitResult.CONTINUE
                }

                override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                    if (exc != null) {
                        throw exc
                    }
                    Files.delete(dir)
                    return FileVisitResult.CONTINUE
                }
            })
        } catch (e: IOException) {
            println("Error occurred while browsing files under " + targetDirectory.toString() + ": " + e.toString())
        }

    }

    /**
     * Write json data to the given target file
     * @param targetFile target file to which json data should be written.
     *                   File may not exist at the time of method invocation, but should not be a directory.
     *
     * @param jsonObject a source json object
     */
    fun writeJson(targetFile: File, jsonObject: JsonElement) {
        if (!targetFile.exists()) {
            val parent = targetFile.parentFile
            if (!parent.exists() && !parent.mkdirs()) {
                throw IOException("Failed to create target directory!")
            }
        }

        Files.newOutputStream(targetFile.toPath()).use { oStream ->
            val writeData = jsonObject.toString().toByteArray(fileFormat)
            oStream.write(writeData)
        }
    }

    /**
     * Read json data from the given target file
     */
    fun readJson(targetFile: File): JsonElement {
        Files.newBufferedReader(targetFile.toPath(), fileFormat)
                .use { reader -> return JsonParser().parse(reader) }
    }

    /**
     * Get the name of the file with it's extension removed.
     * @param file target file
     *
     * @return file name without the extension
     */
    fun getFileBaseName(file: File): String {
        val fileName = file.name
        val lastIndexOfDot = fileName.lastIndexOf(".")
        if (lastIndexOfDot == 0) {
            return fileName
        }
        return fileName.substring(0, lastIndexOfDot)
    }
}