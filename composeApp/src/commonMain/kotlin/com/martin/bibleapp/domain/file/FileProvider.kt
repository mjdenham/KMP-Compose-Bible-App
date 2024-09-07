package com.martin.bibleapp.domain.file

interface FileProvider {
    fun getFilePath(fileName: String): String
}