package org.crosswire.common.util

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM
import okio.buffer
import okio.openZip
import okio.use

class IoUtil {
    fun unpackZip(zipFile: Path, destDir: Path, include: Boolean, vararg includeExclude: String) {
        val zipFileSystem = FileSystem.SYSTEM.openZip(zipFile)
        val fileSystem = FileSystem.SYSTEM

        val paths = zipFileSystem.listRecursively("/".toPath())
            .filter { zipFileSystem.metadata(it).isRegularFile }
            .toList()

        paths.forEach { zipFilePath ->
            zipFileSystem.source(zipFilePath).buffer().use { source ->
                val relativeFilePath = zipFilePath.toString().trimStart('/')
                val fileToWrite = destDir.resolve(relativeFilePath)
                fileToWrite.createParentDirectories()
                fileSystem.sink(fileToWrite).buffer().use { sink ->
                    val bytes = sink.writeAll(source)
                    println("Unzipped: $relativeFilePath to $fileToWrite; $bytes bytes written")
                }
            }
        }
    }
}