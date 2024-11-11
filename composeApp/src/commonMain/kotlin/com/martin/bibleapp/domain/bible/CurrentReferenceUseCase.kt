package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.install.Installation
import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import com.martin.bibleapp.domain.reference.Reference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter

class CurrentReferenceUseCase(private val currentReferenceRepository: CurrentReferenceRepository, private val installation: Installation) {

    fun getCurrentReferenceFlow(): Flow<Reference> = currentReferenceRepository
        .getCurrentReferenceFlow()
        .filter { installation.isInstalled("BSB") }
}