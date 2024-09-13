package com.martin.bibleapp.domain.bible

import com.martin.bibleapp.data.reference.CurrentReferenceRepository
import com.martin.bibleapp.domain.reference.BibleBook
import com.martin.bibleapp.domain.reference.Reference

class ReferenceSelectionUseCase(private val reader: BibleReader, private val currentReferenceRepository: CurrentReferenceRepository) {

    suspend fun getNumChapters(book: BibleBook): Int {
        return reader.countChapters(book)
    }

    suspend fun selectReference(reference: Reference) {
        currentReferenceRepository.updateCurrentReference(reference)
    }
}