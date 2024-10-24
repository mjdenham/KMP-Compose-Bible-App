package com.martin.bibleapp.domain.install

interface Installation {
    suspend fun isInstalled(moduleName: String): Boolean
    suspend fun install(moduleName: String)
}