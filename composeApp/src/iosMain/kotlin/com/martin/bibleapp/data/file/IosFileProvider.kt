package com.martin.bibleapp.data.file

import com.martin.bibleapp.domain.file.FileProvider
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
class IosFileProvider : FileProvider {
    override fun getFilePath(fileName: String): String {
        val urlForUserDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        urlForUserDirectory?.let {
            return "$urlForUserDirectory$fileName"
        } ?: throw RuntimeException("Failed to get iOS User Document directory")
    }
}