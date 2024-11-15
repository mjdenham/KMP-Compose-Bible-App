package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.domain.install.Installation
import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.crosswire.jsword.passage.Verse

class CurrentReferenceUseCase(private val currentReferenceRepository: CurrentReferenceRepository, private val installation: Installation) {

    fun getCurrentReferenceFlow(): Flow<Verse> = currentReferenceRepository
        .getCurrentReferenceFlow()
        .filter { installation.isInstalled("BSB") }
}