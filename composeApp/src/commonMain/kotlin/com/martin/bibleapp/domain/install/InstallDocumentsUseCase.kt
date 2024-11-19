package com.martin.bibleapp.domain.install

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InstallDocumentsUseCase(private val installation: Installation) {
    suspend fun installDocuments() = withContext(Dispatchers.IO) {
        launch {
            if (!installation.isInstalled(BIBLE)) {
                installation.install(BIBLE)
            }
        }
        launch {
            if (!installation.isInstalled(COMMENTARY)) {
                installation.install(COMMENTARY)
            }
        }
    }

    companion object {
        const val BIBLE = "BSB"
        const val COMMENTARY = "KingComments"
    }
}