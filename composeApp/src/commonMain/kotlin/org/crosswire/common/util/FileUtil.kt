package org.crosswire.common.util

import okio.FileSystem
import okio.Path
import okio.SYSTEM

class FileUtil {
    fun deleteDirectory(path: Path) {
        FileSystem.SYSTEM.deleteRecursively(path)
    }

    fun createParentDirectories(path: Path) {
        path.parent?.let { parent ->
            FileSystem.SYSTEM.createDirectories(parent)
        }
    }
}

fun Path.delete() = FileUtil().deleteDirectory(this)

fun Path.createParentDirectories() = FileUtil().createParentDirectories(this)