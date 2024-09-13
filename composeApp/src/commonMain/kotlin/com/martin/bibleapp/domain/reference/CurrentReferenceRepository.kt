package com.martin.bibleapp.domain.reference

import kotlinx.coroutines.flow.Flow

interface CurrentReferenceRepository {
    suspend fun updateCurrentReference(reference: Reference)
    fun getCurrentReferenceFlow(): Flow<Reference>
}