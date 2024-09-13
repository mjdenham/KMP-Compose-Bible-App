package com.martin.bibleapp.fakes

import com.martin.bibleapp.domain.reference.CurrentReferenceRepository
import com.martin.bibleapp.domain.reference.Reference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCurrentReferenceRepository: CurrentReferenceRepository {
    private var currentReference: Reference = Reference.DEFAULT
    override suspend fun updateCurrentReference(reference: Reference) {
        currentReference = reference
    }

    override fun getCurrentReferenceFlow(): Flow<Reference> {
        return flowOf(currentReference)
    }
}