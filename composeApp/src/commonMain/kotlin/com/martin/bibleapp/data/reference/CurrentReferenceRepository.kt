package com.martin.bibleapp.data.reference

import com.martin.bibleapp.data.database.BibleAppDatabase
import com.martin.bibleapp.domain.reference.Reference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CurrentReferenceRepository(bibleAppDatabase: BibleAppDatabase) {
    private val currentReferenceDao = bibleAppDatabase.currentReferenceDao()

    suspend fun updateCurrentReference(reference: Reference) {
        currentReferenceDao.upsert(CurrentReference(CURRENT_REFERENCE_ID, reference.book, reference.chapter, reference.verse))
    }

    fun getCurrentReferenceFlow(): Flow<Reference> {
        return currentReferenceDao.getCurrentReference(CURRENT_REFERENCE_ID)
            .map { if (it != null) Reference(it.book, it.chapter, it.verse) else Reference.DEFAULT }
    }

    companion object {
        const val CURRENT_REFERENCE_ID = 1
    }
}