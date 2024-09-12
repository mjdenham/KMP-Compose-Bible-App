package com.martin.bibleapp.data.reference

import com.martin.bibleapp.data.database.BibleAppDatabase
import com.martin.bibleapp.domain.reference.Reference

class CurrentReferenceRepository(val bibleAppDatabase: BibleAppDatabase) {
    private val currentReferenceDao = bibleAppDatabase.currentReferenceDao()

    suspend fun updateCurrentReference(reference: Reference) {
        currentReferenceDao.upsert(CurrentReference(CURRENT_REFERENCE_ID, reference.book, reference.chapter, reference.verse))
    }

    suspend fun getCurrentReference(): Reference {
        val ref = currentReferenceDao.getCurrentReference(CURRENT_REFERENCE_ID)?.let { currentReference ->
            Reference(currentReference.book, currentReference.chapter, currentReference.verse)
        } ?: Reference.DEFAULT

        return ref
    }

    companion object {
        const val CURRENT_REFERENCE_ID = 1
    }
}