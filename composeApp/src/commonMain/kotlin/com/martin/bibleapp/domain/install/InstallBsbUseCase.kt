package com.martin.bibleapp.domain.install

class InstallBsbUseCase(private val installation: Installation) {
    suspend fun installBsbModule() {
        if (!installation.isInstalled("BSB")) {
            installation.install("BSB")
        }
    }
}